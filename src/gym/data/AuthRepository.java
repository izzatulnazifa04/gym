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

}