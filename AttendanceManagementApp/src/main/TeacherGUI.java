package main;

import com.sun.deploy.config.JREInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.sql.*;
import java.util.Stack;

import static com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table;

public class TeacherGUI {
    private Connection connection;
    private TableView<AttendanceRecord> tableView;
    private ObservableList<AttendanceRecord> attendanceList;
    private String username;

    public TeacherGUI(String username) {
        this.username = username;
    }

    public Scene createScene02() {
        connectToDatabase();

        // Labels and text fields
        Label idLabel = new Label("StudentID:");
        idLabel.setTextFill(Color.ALICEBLUE);
        idLabel.setFont(Font.font("Times New Roman", 20));
        TextField idField = new TextField();
        idField.setStyle("-fx-background-color: #2A3D66; "
                + "-fx-text-fill: white; "
                + "-fx-border-color: #5C7DB5; "
                + "-fx-border-radius: 5px; "
                + "-fx-background-radius: 5px;");


        Label subjectLabel = new Label("SubjectName:");
        subjectLabel.setTextFill(Color.ALICEBLUE);
        subjectLabel.setFont(Font.font("Times New Roman", 20));
        TextField subjectField = new TextField();
        subjectField.setStyle("-fx-background-color: #2A3D66; "
                + "-fx-text-fill: white; "
                + "-fx-border-color: #5C7DB5; "
                + "-fx-border-radius: 5px; "
                + "-fx-background-radius: 5px;");

//        Label dateLabel = new Label("NAME:");
//        dateLabel.setTextFill(Color.ALICEBLUE);
//        dateLabel.setFont(Font.font("Times New Roman", 20));
//        TextField dateField = new TextField();

        Label filterLabel = new Label("Filter By:");
        ComboBox<String> filterComboBox = new ComboBox<>();
        filterComboBox.getItems().addAll("Attendance ID", "Date");
        filterComboBox.setStyle("-fx-background-color: #2A3D66; "
                + "-fx-text-fill: white; "
                + "-fx-border-color: #5C7DB5; "
                + "-fx-border-radius: 5px; "
                + "-fx-background-radius: 5px;");

        filterLabel.setTextFill(Color.ALICEBLUE);
        filterLabel.setFont(Font.font("Times New Roman", 20));
        TextField filterValueField = new TextField();
        filterValueField.setStyle("-fx-background-color: #2A3D66; "
                + "-fx-text-fill: white; "
                + "-fx-border-color: #5C7DB5; "
                + "-fx-border-radius: 5px; "
                + "-fx-background-radius: 5px;");
        filterValueField.setPromptText("Enter Attendance ID or Date");

//        Label passwordLabel = new Label("PASSWORD:");
//        passwordLabel.setTextFill(Color.ALICEBLUE);
//        passwordLabel.setFont(Font.font("Times New Roman", 20));
//        PasswordField passwordField = new PasswordField();

        // Buttons
        Button addButton = new Button("ADD");
        Button editButton = new Button("EDIT");
        Button saveButton = new Button("SAVE");
        Button deleteButton = new Button("DELETE");
        Button ViewButton = new Button("ViewAttendance");
        deleteButton.setDisable(true);

        HBox buttonBox = new HBox(10, saveButton, editButton, addButton, deleteButton, ViewButton);

        // TableView setup
        tableView = new TableView<>();
        attendanceList = FXCollections.observableArrayList();
        tableView.setStyle("-fx-background-color: #5C7DB5; "
                + "-fx-text-fill: white; "
                + "-fx-border-color: #2A3D66; "
                + "-fx-border-radius: 5px; "
                + "-fx-background-radius: 5px;");

        TableColumn<AttendanceRecord, Integer> studentIdColumn = new TableColumn<>("Student ID");
        studentIdColumn.setCellValueFactory(cellData -> cellData.getValue().studentIDProperty().asObject());
        studentIdColumn.setPrefWidth(200);
        studentIdColumn.setStyle("-fx-background-color: #5C7DB5; "
                + "-fx-text-fill: white; "
                + "-fx-border-color: #2A3D66; "
                + "-fx-border-radius: 5px; "
                + "-fx-background-radius: 5px;");

        TableColumn<AttendanceRecord, String> subjectNameColumn = new TableColumn<>("Subject ID");
        subjectNameColumn.setCellValueFactory(cellData -> cellData.getValue().subjectNameProperty());
        subjectNameColumn.setPrefWidth(200);
        subjectNameColumn.setStyle("-fx-background-color: #5C7DB5; "
                + "-fx-text-fill: white; "
                + "-fx-border-color: #2A3D66; "
                + "-fx-border-radius: 5px; "
                + "-fx-background-radius: 5px;");

        TableColumn<AttendanceRecord, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        dateColumn.setPrefWidth(200);
        dateColumn.setStyle("-fx-background-color: #5C7DB5; "
                + "-fx-text-fill: white; "
                + "-fx-border-color: #2A3D66; "
                + "-fx-border-radius: 5px; "
                + "-fx-background-radius: 5px;");

        TableColumn<AttendanceRecord, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        statusColumn.setPrefWidth(200);
        statusColumn.setStyle("-fx-background-color: #5C7DB5; "
                + "-fx-text-fill: white; "
                + "-fx-border-color: #2A3D66; "
                + "-fx-border-radius: 5px; "
                + "-fx-background-radius: 5px;");

        tableView.getColumns().addAll(studentIdColumn, subjectNameColumn, dateColumn, statusColumn);
        tableView.setItems(attendanceList);
        //fetchAttendanceData(studentID, subjectName, filter);
        ViewButton.setOnAction(e -> {
            int studentID = Integer.parseInt(idField.getText());
            String subjectName = subjectField.getText();
            String filterBy = filterComboBox.getValue();
            String filterValue = filterValueField.getText();

            fetchAttendanceData(studentID, subjectName, filterBy, filterValue);
        });

        // Layout
        VBox formBox = new VBox(10, idLabel, idField, subjectLabel, subjectField,
                 filterLabel, filterComboBox, filterValueField, buttonBox);
        formBox.setPadding(new Insets(10));

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

        HBox mainLayout = new HBox(20, formBox, tableView);
        mainLayout.setPadding(new Insets(10));

        StackPane root = new StackPane();
        root.getChildren().addAll(imageView, overlay, mainLayout);

        return new Scene(root, 1300, 750);
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

    private void fetchAttendanceData(int studentID, String subjectName, String filterBy, String filterValue) {

        attendanceList.clear();
        String sql = "SELECT a.studentID, s.subject_Name, a.date, a.status " +
                "FROM Attendance a " +
                "JOIN Subjects s ON a.subjectID = s.subjectID " +
                "WHERE a.studentID = ? AND s.subject_Name = ? AND ";

        if ("Attendance ID".equals(filterBy)) {
            sql += "a.attendanceID = ?";
        } else {
            sql += "a.date = ?";
        }

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, studentID);
            pstmt.setString(2, subjectName);
            pstmt.setString(3, filterValue);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                attendanceList.add(new AttendanceRecord(
                        rs.getInt("studentID"),
                        rs.getString("subject_Name"),
                        rs.getString("date"),
                        rs.getString("status")
                ));
            }
            tableView.setItems(attendanceList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
