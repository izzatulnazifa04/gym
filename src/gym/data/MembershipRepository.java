package gym.data;

import gym.model.Membership;
import gym.model.MembershipStatus;
import gym.model.MonthlyMembership;
import gym.model.YearlyMembership;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Reads and writes membership records in SQLite. */
public final class MembershipRepository {
    private final Path database;

    public MembershipRepository(Path database) throws Exception {
        this.database = database.toAbsolutePath().normalize();

        if (this.database.getParent() != null) {
            Files.createDirectories(this.database.getParent());
        }

        Class.forName("org.sqlite.JDBC");
        createTableIfNeeded();
    }