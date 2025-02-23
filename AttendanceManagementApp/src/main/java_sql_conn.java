package main;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class java_sql_conn {
    public static void conn() {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=AttendanceDB;encrypt=true;trustServerCertificate=true";
        String user = "SA";  // Your SQL Server username
        String password = "1234";  // Your SQL Server password

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to SQL Server successfully!");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
