package gym.data;

import gym.auth.PasswordHasher;
import gym.model.UserAccount;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** Stores users and checks login details. */
public final class AuthRepository {
    private final Path database;

    public AuthRepository(Path database) throws Exception {
        this.database = database.toAbsolutePath().normalize();

        if (this.database.getParent() != null) {
            Files.createDirectories(this.database.getParent());
        }

        Class.forName("org.sqlite.JDBC");
        createUsersTableIfNeeded();
        seedDemoUser();
    }

    // Create users table if it doesn't exist
    private void createUsersTableIfNeeded() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL UNIQUE COLLATE NOCASE, "
                + "password_hash TEXT NOT NULL, "
                + "salt TEXT NOT NULL, "
                + "role TEXT NOT NULL DEFAULT 'STAFF', "
                + "active INTEGER NOT NULL DEFAULT 1 CHECK(active IN (0,1))"
                + ")";

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }
    
    private Connection connect() throws SQLException {
        String url = "jdbc:sqlite:" + database;
        Connection connection = DriverManager.getConnection(url);

        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA busy_timeout=3000");
        }
        return connection;
    }

    // Seeds the database with a default admin user.
    private void seedDemoUser() throws SQLException {
        String salt = PasswordHasher.newSalt();
        String hash = PasswordHasher.hash("admin123".toCharArray(), salt);
        String sql = "INSERT OR IGNORE INTO users "
                + "(username, password_hash, salt, role, active) VALUES (?, ?, ?, ?, 1)";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "admin");
            statement.setString(2, hash);
            statement.setString(3, salt);
            statement.setString(4, "ADMIN");
            statement.executeUpdate();
        }
    }

    public UserAccount authenticate(String username, char[] password){
        throws SQLException {
        if (username == null || username.trim().isEmpty()
                || password == null || password.length == 0) {
            return null;
        }

        String sql = "SELECT username, password_hash, salt, role "
                + "FROM users WHERE username=? AND active=1";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username.trim());

            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return null;
                }

                String savedHash = result.getString("password_hash");
                String savedSalt = result.getString("salt");
                boolean correctPassword = PasswordHasher.matches(
                        password, savedHash, savedSalt);

                if (!correctPassword) {
                    return null;
                }

                return new UserAccount(result.getString("username"),
                        result.getString("role"));
            }
        }
    }

    public Path getDatabase() {
        return database;
    }
}

