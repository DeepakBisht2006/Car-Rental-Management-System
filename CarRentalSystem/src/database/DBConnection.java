package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton-style utility class that manages the JDBC connection
 * to the car_rental_system MySQL database.
 *
 * HOW TO CONFIGURE:
 *   1. Set DB_URL to point to your MySQL server.
 *   2. Set DB_USER and DB_PASSWORD to your MySQL credentials.
 *   3. Ensure the MySQL Connector/J JAR is on your classpath.
 */
public class DBConnection {

    // ── Connection parameters ────────────────────────────────────────────────────

    private static final String DB_URL = "jdbc:mysql://localhost:3306/car_rental_system"
                                   + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DB_USER     = "root";       
    private static final String DB_PASSWORD = "dis11169";   

    /** Cached connection – reused across the application session. */
    private static Connection connection = null;

    // ── Private constructor – utility class, no instantiation ────────────────────

    private DBConnection() {}

    // ── Public API ───────────────────────────────────────────────────────────────

    /**
     * Returns a live Connection to the database.
     * Creates a new connection if none exists or the existing one is closed.
     *
     * @return  {@link Connection} to car_rental_system
     * @throws  SQLException if the connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load the MySQL JDBC driver (required for older driver versions)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                "MySQL JDBC Driver not found.\n" +
                "Add mysql-connector-j-x.x.x.jar to your classpath.", e);
        }

        // Create a new connection if one doesn't exist or has been closed
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("[DBConnection] Connected to database successfully.");
        }
        return connection;
    }

    /**
     * Closes the shared connection.
     * Call this when the application shuts down.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("[DBConnection] Connection closed.");
            } catch (SQLException e) {
                System.err.println("[DBConnection] Error closing connection: " + e.getMessage());
            }
        }
    }
}
