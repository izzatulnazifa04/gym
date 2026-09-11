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