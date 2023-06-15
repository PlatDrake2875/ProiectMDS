package Proiect.MDS.web.database;

import Proiect.MDS.web.database.logging.ProductLogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class handles operations related to the database connection.
 */
public class DatabaseConnection {
    private static final Logger LOGGER = new ProductLogger(Database.class).getLogger();
    private Connection connection;

    /**
     * Constructor creates a connection to the database.
     * @param url The database url.
     * @param username The database username.
     * @param password The database password.
     */
    public DatabaseConnection(String url, String username, String password) {
        try {
            connection = DriverManager.getConnection(url, username, password);
            LOGGER.log(Level.INFO, "Connected to the MySQL database");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error connecting to the MySQL database:", e);
        }
    }

    /**
     * @return The database connection object.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Close the database connection.
     */
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error closing the MySQL database connection:", e);
        }
    }
}
