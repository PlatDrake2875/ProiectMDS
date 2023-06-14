package Proiect.MDS.web.database;

import Proiect.MDS.web.models.Product;
import Proiect.MDS.web.database.logging.ProductLogger;
import Proiect.MDS.web.database.logging.DatabaseLogger;

import java.sql.*;

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