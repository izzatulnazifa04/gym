package gym.model;

/** Represents the user who has passed the login check. */
public final class UserAccount {
    private final String username;
    private final String role;

    public UserAccount(String username, String role) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }

        this.username = username.trim();
        this.role = role == null || role.trim().isEmpty()
                ? "STAFF" : role.trim();
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}
