package main;

import database.DBConnection;
import ui.LoginForm;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Application entry point — verifies DB connection then shows the Login screen.
 */
public class Main {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            // non-critical
        }

        // Verify DB connectivity before launching UI
        try {
            Connection conn = DBConnection.getConnection();
            if (conn == null || conn.isClosed()) throw new SQLException("Connection returned null.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                "Cannot connect to the database.\n\n" +
                "Please verify:\n" +
                "  • MySQL server is running.\n" +
                "  • 'car_rental_system' database exists (run schema.sql).\n" +
                "  • Credentials in DBConnection.java are correct.\n" +
                "  • MySQL Connector/J JAR is on the classpath.\n\n" +
                "Error: " + ex.getMessage(),
                "Database Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Launch Login screen on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
