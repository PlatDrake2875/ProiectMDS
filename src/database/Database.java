package database;

import DataModel.Product;
import database.logging.ProductLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import database.logging.DatabaseLogger;

public class Database {
    private static final DatabaseLogger LOGGER = new DatabaseLogger(Database.class);
    Connection connection;
    private Statement stmt;

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
