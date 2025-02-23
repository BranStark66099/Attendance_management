package main;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.sql.*;

public class Edit_Attendance_byTeacher{
    private TableView<AttendanceRecord> table;
    private TextField studentIDField, subjectIDField, dateField, statusField;
    private ObservableList<AttendanceRecord> attendanceList;
    private Connection connection;

    public Edit_Attendance_byTeacher(){

    }

    public Scene createScene03() {
        connectToDatabase();

        table = new TableView<>();
        attendanceList = FXCollections.observableArrayList();

        loadAttendanceData();
        table.setStyle("-fx-alignment: CENTER;" + "-fx-background-color: #5C7DB5; "
                + "-fx-border-color: #696969; "
                + "-fx-border-radius: 5px; ");
        TableColumn<AttendanceRecord, Integer> studentIDCol = new TableColumn<>("Student ID");
        studentIDCol.setCellValueFactory(cellData -> cellData.getValue().studentIDProperty().asObject());
        studentIDCol.setPrefWidth(100);
        studentIDCol.setStyle("-fx-alignment: CENTER;" + "-fx-background-color: #5C7DB5; "
                + "-fx-border-color: #696969; "
                + "-fx-border-radius: 5px; ");

        TableColumn<AttendanceRecord, String> subjectNameCol = new TableColumn<>("Subject Name");
        subjectNameCol.setCellValueFactory(cellData -> cellData.getValue().subjectNameProperty());
        subjectNameCol.setPrefWidth(300);
        subjectNameCol.setStyle("-fx-alignment: CENTER;" + "-fx-background-color: #5C7DB5; "
                + "-fx-border-color: #696969; "
                + "-fx-border-radius: 5px; ");

        TableColumn<AttendanceRecord, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        dateCol.setPrefWidth(300);
        dateCol.setStyle("-fx-alignment: CENTER;" + "-fx-background-color: #5C7DB5; "
                + "-fx-border-color: #696969; "
                + "-fx-border-radius: 5px; ");

        TableColumn<AttendanceRecord, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        statusCol.setPrefWidth(296);
        statusCol.setStyle("-fx-alignment: CENTER;" + "-fx-background-color: #5C7DB5; "
                + "-fx-border-color: #696969; "
                + "-fx-border-radius: 5px; ");

        table.getColumns().addAll(studentIDCol, subjectNameCol, dateCol, statusCol);
        table.setItems(attendanceList);

        studentIDField = new TextField();
        studentIDField.setPromptText("Student ID");

        subjectIDField = new TextField();
        subjectIDField.setPromptText("Subject ID");

        dateField = new TextField();
        dateField.setPromptText("Date (YYYY-MM-DD)");

        statusField = new TextField();
        statusField.setPromptText("Status");

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> addAttendanceRecord());

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> updateAttendanceRecord());

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> deleteAttendanceRecord());

        HBox inputBox = new HBox(10, studentIDField, subjectIDField, dateField, statusField, addButton, updateButton, deleteButton);
        inputBox.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(25, table, inputBox);

        // Load background image
        Image image = new Image(getClass().getResource("/minimalist-4k-wallpaper-hd-26.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(1300);
        imageView.setFitHeight(750);
        imageView.setPreserveRatio(false);

        // Apply blur effect to background
        GaussianBlur blur = new GaussianBlur(20);
        imageView.setEffect(blur);

        Rectangle overlay = new Rectangle(1300, 750, Color.BLACK);
        overlay.setOpacity(0.4);

        StackPane root = new StackPane();
        root.getChildren().addAll(imageView, overlay, vBox);
        root.setAlignment(Pos.CENTER);

        return new Scene(root, 1000, 500);
    }

    private void connectToDatabase() {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=AttendanceDB;encrypt=true;trustServerCertificate=true";
        String user = "SA";
        String password = "1234";

        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Database Connected!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAttendanceData() {
        attendanceList.clear();
        String query = "SELECT a.*, s.subject_Name FROM Attendance a JOIN Subjects s on a.subjectID = s.subjectID";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                attendanceList.add(new AttendanceRecord(
                        rs.getInt("studentID"),
                        rs.getString("subject_Name"),
                        rs.getString("date"),
                        rs.getString("status")
                ));
            }
            table.setItems(attendanceList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addAttendanceRecord() {
        String query = "INSERT INTO Attendance (studentID, subjectID, date, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, studentIDField.getText());
            pstmt.setString(2, subjectIDField.getText());
            pstmt.setString(3, dateField.getText());
            pstmt.setString(4, statusField.getText());
            pstmt.executeUpdate();
            loadAttendanceData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateAttendanceRecord() {
        String query = "UPDATE Attendance SET subjectID = ?, date = ?, status = ? WHERE studentID = ? AND date = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, subjectIDField.getText());
            pstmt.setString(2, dateField.getText());
            pstmt.setString(3, statusField.getText());
            pstmt.setString(4, studentIDField.getText());
            pstmt.setString(5, dateField.getText());
            pstmt.executeUpdate();
            loadAttendanceData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteAttendanceRecord() {
        String query = "DELETE FROM Attendance WHERE studentID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, studentIDField.getText());
            pstmt.setString(2, dateField.getText());
            pstmt.executeUpdate();
            loadAttendanceData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
