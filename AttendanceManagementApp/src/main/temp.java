package main;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class temp extends Application {

    private TableView<AttendanceRecord> table = new TableView<>();
    private ObservableList<AttendanceRecord> data = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student Attendance");

        // Labels and Inputs
        Label studentIdLabel = new Label("Student ID:");
        TextField studentIdField = new TextField();

        Label subjectLabel = new Label("Subject Name:");
        TextField subjectField = new TextField();

        Label filterLabel = new Label("Filter By:");
        ComboBox<String> filterComboBox = new ComboBox<>();
        filterComboBox.getItems().addAll("Attendance ID", "Date");

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

        primaryStage.setScene(new Scene(vbox, 500, 500));
        primaryStage.show();
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

    public static void main(String[] args) {
        launch(args);
    }
}