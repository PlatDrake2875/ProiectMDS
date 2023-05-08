package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/dbProducts?useSSL=false&allowPublicKeyRetrieval=true";

        try (Connection connection = DriverManager.getConnection(url, "root", "2875");
             Statement stmt = connection.createStatement()) {

            LOGGER.log(Level.INFO, "Connected to the MySQL database!");

            createTables(stmt);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error connecting to the MySQL database:", e);
        }
    }

    public static void createTables(Statement stmt) throws SQLException {
        String productTableSQL = """
                CREATE TABLE IF NOT EXISTS products (
                     id INT AUTO_INCREMENT PRIMARY KEY,
                     name VARCHAR(255) NOT NULL,
                     price DOUBLE NOT NULL,
                     quantity INT NOT NULL
                 );
                """;

        stmt.execute(productTableSQL);
    }

    public static void insertProduct(Statement stmt, String name, Double price, Integer quantity) throws SQLException {
        String insertProductSQL = """
                INSERT INTO products (name, price, quantity)
                VALUES ('%s', %f, %d);
                """.formatted(name, price, quantity);

        stmt.execute(insertProductSQL);
    }

}
