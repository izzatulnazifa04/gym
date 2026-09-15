package gym;

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

    // CREATE - insert a new membership record, including rate and discount.
    public static boolean addMembership(Membership membership) {
        String sql = "INSERT INTO memberships (member_name, ic_number, membership_type, start_date, rate, discount) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, membership.getMemberName());
            pstmt.setString(2, membership.getIcNumber());
            pstmt.setString(3, membership.getMembershipType());
            pstmt.setString(4, membership.getStartDate());
            pstmt.setDouble(5, membership.getRate());
            pstmt.setDouble(6, membership.getDiscountPercent());
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Add membership error: " + e.getMessage());
            return false;
        }
    }

     // READ - retrieve all membership records, reconstructing rate/discount too.
    public static List<Membership> getAllMemberships() {
        List<Membership> list = new ArrayList<>();
        String sql = "SELECT * FROM memberships ORDER BY member_id";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("member_id");
                String name = rs.getString("member_name");
                String ic = rs.getString("ic_number");
                String type = rs.getString("membership_type");
                String startDate = rs.getString("start_date");
                double rate = rs.getDouble("rate");
                double discount = rs.getDouble("discount");

                Membership m;
                if (type.equals("Yearly")) {
                    m = new YearlyMembership(id, name, ic, startDate, rate, discount);
                } else {
                    m = new MonthlyMembership(id, name, ic, startDate, rate);
                }
                list.add(m);
            }

        } catch (SQLException e) {
            System.out.println("Retrieve error: " + e.getMessage());
        }
        return list;
    }

    // UPDATE - update an existing membership record by ID, including rate/discount.
    public static boolean updateMembership(Membership membership) {
        String sql = "UPDATE memberships SET member_name = ?, ic_number = ?, "
                + "membership_type = ?, start_date = ?, rate = ?, discount = ? WHERE member_id = ?";
        
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, membership.getMemberName());
            pstmt.setString(2, membership.getIcNumber());
            pstmt.setString(3, membership.getMembershipType());
            pstmt.setString(4, membership.getStartDate());
            pstmt.setDouble(5, membership.getRate());
            pstmt.setDouble(6, membership.getDiscountPercent());
            pstmt.setInt(7, membership.getMemberID());
            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Update error: " + e.getMessage());
            return false;
        }
    }

    // DELETE - remove a membership record by ID.
    public static boolean deleteMembership(int memberID) {
        String sql = "DELETE FROM memberships WHERE member_id = ?";