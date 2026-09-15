package gym;

import javax.swing.SwingUtilities; //import data dari gui toolkit

/**
 * Main - application entry point.
 * Initializes the database tables, then shows the staff Login form
 * first. GymMembershipForm only opens after a successful login.
 */
public class Main {
    public static void main(String[] args) {
        DatabaseHelper.initializeDatabase(); // untuk pstikan database connected and ready to go
        DatabaseHelper.initializeStaffTable(); // utk pstikan table exists dan staff boleh log in

        SwingUtilities.invokeLater(() -> {  // use invokeLater to safely fire up the UI
            LoginForm login = new LoginForm();
            login.setVisible(true);
        });
    }
}