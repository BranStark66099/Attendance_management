package main;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class StudentGUIController {

    @FXML private TextField studentIdField;
    @FXML private TextField subjectField;
    @FXML private ComboBox<String> filterComboBox;
    @FXML private TextField filterValueField;
    @FXML private Button fetchButton;
    @FXML private TableView<AttendanceRecord> attendanceTable;
    @FXML private TableColumn<AttendanceRecord, Integer> studentIdCol;
    @FXML private TableColumn<AttendanceRecord, String> subjectCol;
    @FXML private TableColumn<AttendanceRecord, String> dateCol;
    @FXML private TableColumn<AttendanceRecord, String> statusCol;

    private ObservableList<AttendanceRecord> data = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Set up the columns for the TableView
        studentIdCol.setCellValueFactory(cellData -> cellData.getValue().studentIDProperty().asObject());
        subjectCol.setCellValueFactory(cellData -> cellData.getValue().subjectNameProperty());
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        statusCol.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        // Add filter options to the ComboBox
        filterComboBox.getItems().addAll("Attendance ID", "Date");
    }

    @FXML
    private void handleFetchButton() {
        // Get the input values from the UI
        int studentID = Integer.parseInt(studentIdField.getText());
        String subjectName = subjectField.getText();
        String filterBy = filterComboBox.getValue();
        String filterValue = filterValueField.getText();

        // Fetch attendance data
        fetchAttendance(studentID, subjectName, filterBy, filterValue);
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

            attendanceTable.setItems(data);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
