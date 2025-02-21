package main;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TeacherGUI {
    private String FirstName, LastName;
    private Connection connection;
    private String username;

    public TeacherGUI(String username){
        this.username = username;
    }
    public Scene createScene02(){
        connectToDatabase();
        StackPane root = new StackPane();
        return new Scene(root, 1500, 750);
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
}
