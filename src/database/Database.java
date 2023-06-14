package database;

import DataModel.Product;
import database.logging.ProductLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import database.logging.DatabaseLogger;

/**
 * Provides functionality to connect to a MySQL database and perform operations on the 'products' table.
 */
public class Database {
    private static final DatabaseLogger LOGGER = new DatabaseLogger(Database.class);
    Connection connection;
    private Statement stmt;

    /**
     * Creates a new instance of the Database class and establishes a connection to the MySQL database.
     * Also, creates a 'products' table if it does not exist.
     *
     * @param url      the URL of the MySQL database
     * @param username the username of the MySQL database
     * @param password the password of the MySQL database
     */
    public Database(String url, String username, String password) {
        try {
            connection = DriverManager.getConnection(url, username, password);
            stmt = connection.createStatement();
            stmt.execute(ProductTableOperations.CREATE_TABLE_SQL);
            LOGGER.logSuccessfulOperation("Connection to the MySQL database and creation of the 'products' table");
        } catch (SQLException e) {
            LOGGER.logDatabaseConnectionError(e);
        }
    }

    /**
     * Closes the connection to the MySQL database and the Statement used for executing SQL commands.
     */
    public void closeConnection() {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.logDatabaseDisconnectionError(e);
        }
    }
}