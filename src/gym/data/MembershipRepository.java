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

    private void createTableIfNeeded() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS memberships ("
                + "member_id TEXT PRIMARY KEY, "
                + "member_name TEXT NOT NULL CHECK(length(trim(member_name)) BETWEEN 1 AND 80), "
                + "ic_number TEXT NOT NULL UNIQUE, "
                + "phone_number TEXT NOT NULL, "
                + "start_date TEXT NOT NULL, "
                + "expiry_date TEXT NOT NULL, "
                + "membership_type TEXT NOT NULL CHECK(membership_type IN ('MONTHLY','YEARLY')), "
                + "status TEXT NOT NULL CHECK(status IN ('ACTIVE','EXPIRED','SUSPENDED')), "
                + "rate_cents INTEGER NOT NULL CHECK(typeof(rate_cents)='integer' AND rate_cents BETWEEN 1 AND 100000000), "
                + "discount_percent INTEGER NOT NULL DEFAULT 0 CHECK(discount_percent BETWEEN 0 AND 100)"
                + ")";
        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    public Path getDatabase() {
        return database;
    }

    private Connection connect() throws SQLException {
        String url = "jdbc:sqlite:" + database;
        Connection connection = DriverManager.getConnection(url);

        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA busy_timeout=3000");
        }
        return connection;
    }

     public void insert(Membership membership) throws SQLException {
        String sql = "INSERT INTO memberships(member_id, member_name, ic_number, "
                + "phone_number, start_date, expiry_date, membership_type, status, "
                + "rate_cents, discount_percent) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            bindMembership(statement, membership);
            statement.executeUpdate();
        }
    }
    
     public void update(Membership membership) throws SQLException {
        String sql = "UPDATE memberships SET member_name=?, ic_number=?, "
                + "phone_number=?, start_date=?, expiry_date=?, membership_type=?, "
                + "status=?, rate_cents=?, discount_percent=? WHERE member_id=?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, membership.getMemberName());
            statement.setString(2, membership.getIcNumber());
            statement.setString(3, membership.getPhoneNumber());
            statement.setString(4, membership.getStartDate().toString());
            statement.setString(5, membership.getExpiryDate().toString());
            statement.setString(6, membership.getMembershipType());
            statement.setString(7, membership.getStatus().name());
            statement.setLong(8, getRateCents(membership));
            statement.setInt(9, getDiscountPercent(membership));
            statement.setString(10, membership.getMemberId());

            if (statement.executeUpdate() != 1) {
                throw new SQLException("Member no longer exists. Refresh the table.");
            }
        }
    }