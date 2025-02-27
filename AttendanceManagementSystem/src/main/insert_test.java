package main;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class insert_test {

    public static void main(String[] args) {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=AttendanceDB;encrypt=true;trustServerCertificate=true";
        String user = "SA";  // Change this to your SQL Server username
        String password = "1234";  // Change this to your password

        // SQL Insert Query
        String sql = "INSERT INTO Students (FirstName, LastName, Class, GPA) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set values for placeholders
            pstmt.setString(1, "John");  // Name
            pstmt.setString(2, "Iran");
            pstmt.setString(3, "Maths");  //
            pstmt.setDouble(4, 3.75);  // GPA

            // Execute the insert query
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✅ Data inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

