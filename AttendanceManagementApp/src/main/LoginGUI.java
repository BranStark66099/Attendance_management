package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.nio.file.FileStore;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class LoginGUI extends Application {
    private TextField username_text;
    private PasswordField password_text;
    private Connection connection;
    private String username;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Attendance Management System");

        // Database connection
        connectToDatabase();
        //java_sql_conn.conn();

        // Labels
        Label usernamelabel = new Label("Username");
        Label passwordlabel = new Label("Password");
        usernamelabel.setFont(Font.font("Times New Roman", 20));
        usernamelabel.setTextFill(Color.ALICEBLUE);
        passwordlabel.setFont(Font.font("Times New Roman", 20));
        passwordlabel.setTextFill(Color.ALICEBLUE);

        // Text fields & buttons
        username_text = new TextField();
        password_text = new PasswordField();
        Button loginStudentButton = new Button("Login as Student");
        Button loginTeacherButton = new Button("Login as Teacher");

        // GridPane for form fields
        GridPane gPane = new GridPane();
        gPane.setHgap(10);
        gPane.setVgap(10);
        gPane.add(usernamelabel, 0, 0);
        gPane.add(username_text, 1, 0);
        gPane.add(passwordlabel, 0, 1);
        gPane.add(password_text, 1, 1);
        gPane.setAlignment(Pos.CENTER);
        gPane.getTransforms().add(new Translate(0, -50));

        // HBox for buttons
        HBox hBox3 = new HBox(10, loginStudentButton, loginTeacherButton);
        hBox3.setAlignment(Pos.CENTER);
        hBox3.getTransforms().add(new Translate(0, -200));

        // BorderPane to hold layout
        BorderPane pane = new BorderPane();
        pane.setCenter(gPane);
        pane.setBottom(hBox3);

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
        root.getChildren().addAll(imageView, overlay, pane);

        // Set scene
        Scene scene = new Scene(root, 1000, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Set button actions
        loginStudentButton.setOnAction(e -> {
            try {
                handleLogin(primaryStage, "student");
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
        });
        loginTeacherButton.setOnAction(e -> {
            try {
                handleLogin(primaryStage, "teacher");
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    // Connect to Database
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

    // Handle Login
    private void handleLogin(Stage stage, String role) throws NoSuchAlgorithmException {
        String username = username_text.getText();
        String password = password_text.getText();
        //String h_password = hash_pass.hashPassword(password);

        if (authenticateUser(username, password, role)) {
            if(role.equals("teacher")){

                switchToStudentDashboard(stage, username);
            }else{
                ///switchToTeacherDashboard(stage, username);
            }
        } else {
            showAlert("❌ Invalid Credentials", "Please check your username and password.");
        }
    }

    // Authenticate User from Database
    private boolean authenticateUser(String username, String password, String role) throws NoSuchAlgorithmException {
        String tableName = role.equals("student") ? "Students" : "Teachers";
        String query = "SELECT * FROM " + tableName + " WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // If a row exists, authentication is successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Switch to Respective Dashboard
    private void switchToStudentDashboard(Stage stage, String username) {
        try {
            StudentGUI studentGUI = new StudentGUI(username);
            Scene studentScene = studentGUI.createScene();
            stage.setScene(studentScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Show Alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
