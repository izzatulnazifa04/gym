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