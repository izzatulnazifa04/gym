package gmms;

import javax.swing.SwingUtilities;

/**
 * Main - application entry point.
 * Initializes the database tables, then shows the staff Login form
 * first. GymMembershipForm only opens after a successful login.
 */
public class Main {
    public static void main(String[] args) {
        DatabaseHelper.initializeDatabase();
        DatabaseHelper.initializeStaffTable();

        SwingUtilities.invokeLater(() -> {
            LoginForm login = new LoginForm();
            login.setVisible(true);
        });
    }
}