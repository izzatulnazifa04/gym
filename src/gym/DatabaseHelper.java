package gmms;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseHelper manages the connection to the SQLite database and
 * performs CRUD operations for the "memberships" table.
 */
public class DatabaseHelper {

    private static final String URL = "jdbc:sqlite:gym.db";

    public static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS memberships ("
                + "member_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "member_name TEXT NOT NULL, "
                + "ic_number TEXT NOT NULL, "
                + "membership_type TEXT NOT NULL, "
                + "start_date TEXT NOT NULL, "
                + "rate REAL NOT NULL DEFAULT 80.0, "
                + "discount REAL NOT NULL DEFAULT 0.0"
                + ")";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Database initialization error: " + e.getMessage());
        }
    }

    public static void initializeStaffTable() {
        String createSql = "CREATE TABLE IF NOT EXISTS staff ("
                + "staff_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL UNIQUE, "
                + "password TEXT NOT NULL"
                + ")";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute(createSql);

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM staff");
            if (rs.next() && rs.getInt("total") == 0) {
                stmt.execute("INSERT INTO staff (username, password) VALUES ('admin', 'admin123')");
            }

        } catch (SQLException e) {
            System.out.println("Staff table initialization error: " + e.getMessage());
        }
    }

    public static boolean validateLogin(String username, String password) {
        String sql = "SELECT * FROM staff WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.out.println("Login validation error: " + e.getMessage());
            return false;
        }
    }