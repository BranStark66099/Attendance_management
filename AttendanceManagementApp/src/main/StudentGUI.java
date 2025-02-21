package main;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class StudentGUI {

    private TableView<AttendanceRecord> table = new TableView<>();
    private ObservableList<AttendanceRecord> data = FXCollections.observableArrayList();
    private String username;

    public StudentGUI(String username) {
        this.username = username;
    }

    public Scene createScene() {
        // Labels and Inputs
        Label studentIdLabel = new Label("Student ID: " + username);  // Display username or studentID
        TextField studentIdField = new TextField(username);  // Optional, auto-fill field
        studentIdLabel.setTextFill(Color.ALICEBLUE);
        studentIdLabel.setFont(Font.font("Times New Roman", 20));

        Label subjectLabel = new Label("Subject Name:");
        TextField subjectField = new TextField();
        subjectLabel.setTextFill(Color.ALICEBLUE);
        subjectLabel.setFont(Font.font("Times New Roman", 20));

        Label filterLabel = new Label("Filter By:");
        ComboBox<String> filterComboBox = new ComboBox<>();
        filterComboBox.getItems().addAll("Attendance ID", "Date");
        filterLabel.setTextFill(Color.ALICEBLUE);
        filterLabel.setFont(Font.font("Times New Roman", 20));

        TextField filterValueField = new TextField();
        filterValueField.setPromptText("Enter Attendance ID or Date");

        Button fetchButton = new Button("Fetch Attendance");

        // Table Columns
        TableColumn<AttendanceRecord, Integer> studentIdCol = new TableColumn<>("Student ID");
        studentIdCol.setCellValueFactory(cellData -> cellData.getValue().studentIDProperty().asObject());

        TableColumn<AttendanceRecord, String> subjectCol = new TableColumn<>("Subject Name");
        subjectCol.setCellValueFactory(cellData -> cellData.getValue().subjectNameProperty());

        TableColumn<AttendanceRecord, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

        TableColumn<AttendanceRecord, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        table.getColumns().addAll(studentIdCol, subjectCol, dateCol, statusCol);

        // Fetch Data Button Action
        fetchButton.setOnAction(e -> {
            int studentID = Integer.parseInt(studentIdField.getText());
            String subjectName = subjectField.getText();
            String filterBy = filterComboBox.getValue();
            String filterValue = filterValueField.getText();

            fetchAttendance(studentID, subjectName, filterBy, filterValue);
        });

        // Layout
        VBox vbox = new VBox(10, studentIdLabel, studentIdField, subjectLabel, subjectField,
                filterLabel, filterComboBox, filterValueField, fetchButton, table);
        vbox.setPadding(new Insets(20));
        // Load background image
        Image image = new Image(getClass().getResource("/minimalist-4k-wallpaper-hd-26.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(1000);
        imageView.setFitHeight(500);
        imageView.setPreserveRatio(false);

        // Apply blur effect to background
        GaussianBlur blur = new GaussianBlur(20);
        imageView.setEffect(blur);

        Rectangle overlay = new Rectangle(1000, 500, Color.BLACK);
        overlay.setOpacity(0.4);

        // StackPane to layer background and UI
        StackPane root = new StackPane();
        root.getChildren().addAll(imageView, overlay, vbox);
        return new Scene(root, 1000, 500);  // Return the Scene
    }



    private void fetchAttendance(int studentID, String subjectName, String filterBy, String filterValue) {
        data.clear();

        String url = "jdbc:sqlserver://localhost:1433;databaseName=AttendanceDB;encrypt=true;trustServerCertificate=true";
        String user = "SA";
        String password = "1234";

        String sql = "SELECT a.studentID, s.subject_Name, a.date, a.status " +
                "FROM Attendance a " +
                "JOIN Subjects s ON a.subjectID = s.subjectID " +
                "WHERE a.studentID = ? AND s.subject_Name = ? AND ";

        if ("Attendance ID".equals(filterBy)) {
            sql += "a.attendanceID = ?";
        } else {
            sql += "a.date = ?";
        }

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentID);
            pstmt.setString(2, subjectName);
            pstmt.setString(3, filterValue);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                data.add(new AttendanceRecord(
                        rs.getInt("studentID"),
                        rs.getString("subject_Name"),
                        rs.getString("date"),
                        rs.getString("status")
                ));
            }
            table.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}