package database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());
    private Connection connection;
    private Statement stmt;

    public Database(String url, String username, String password) {
        try {
            connection = DriverManager.getConnection(url, username, password);
            stmt = connection.createStatement();

            LOGGER.log(Level.INFO, "Connected to the MySQL database!");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error connecting to the MySQL database:", e);
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
            LOGGER.log(Level.SEVERE, "Error closing the MySQL database connection:", e);
        }
    }


    public void createTable() {
        String createTableSQL = """
                CREATE TABLE products (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    category VARCHAR(255) NOT NULL,
                    price DECIMAL(10, 2) NOT NULL,
                    product_type VARCHAR(255),
                    storage_conditions VARCHAR(255),
                    weight DECIMAL(10, 2),
                    shelf_life VARCHAR(255),
                    ingredients TEXT,
                    kcal_per_100g DECIMAL(10, 2),
                    kj_per_100g DECIMAL(10, 2),
                    fats DECIMAL(10, 2),
                    saturated_fats DECIMAL(10, 2),
                    carbohydrates DECIMAL(10, 2),
                    sugars DECIMAL(10, 2),
                    salt DECIMAL(10, 2),
                    fiber DECIMAL(10, 2),
                    proteins DECIMAL(10, 2)
                );

                """;
        try {
            stmt.execute(createTableSQL);
            LOGGER.log(Level.INFO, "Table 'products' created successfully");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating 'products' table:", e);
        }
    }

    public void insertProduct(String name, String category, BigDecimal price, String product_type, String storage_conditions, BigDecimal weight, String shelf_life, String ingredients, BigDecimal kcal_per_100g, BigDecimal kj_per_100g, BigDecimal fats, BigDecimal saturated_fats, BigDecimal carbohydrates, BigDecimal sugars, BigDecimal salt, BigDecimal fiber, BigDecimal proteins) {
        String insertSQL = """
                INSERT INTO products (name, category, price,
                product_type, storage_conditions, weight,
                shelf_life, ingredients, kcal_per_100g,
                kj_per_100g, fats, saturated_fats,
                carbohydrates, sugars, salt,
                fiber, proteins) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            setProduct(name, category, price, product_type, storage_conditions, weight, shelf_life, ingredients, kcal_per_100g, kj_per_100g, fats, saturated_fats, carbohydrates, sugars, salt, fiber, proteins, pstmt);

            pstmt.executeUpdate();
            LOGGER.log(Level.INFO, "Product inserted successfully");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting product:", e);
        }
    }

    public void updateProduct(int id, String name, String category, BigDecimal price, String product_type, String storage_conditions, BigDecimal weight, String shelf_life, String ingredients, BigDecimal kcal_per_100g, BigDecimal kj_per_100g, BigDecimal fats, BigDecimal saturated_fats, BigDecimal carbohydrates, BigDecimal sugars, BigDecimal salt, BigDecimal fiber, BigDecimal proteins) {
        String updateSQL = """
                UPDATE products SET
                name = ?, category = ?, price = ?,
                product_type = ?, storage_conditions = ?, weight = ?,
                shelf_life = ?, ingredients = ?, kcal_per_100g = ?,
                kj_per_100g = ?, fats = ?, saturated_fats = ?,
                carbohydrates = ?, sugars = ?, salt = ?,
                 fiber = ?, proteins = ? WHERE id = ?;
                """;
        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            setProduct(name, category, price, product_type, storage_conditions, weight, shelf_life, ingredients, kcal_per_100g, kj_per_100g, fats, saturated_fats, carbohydrates, sugars, salt, fiber, proteins, pstmt);
            pstmt.setInt(18, id);

            pstmt.executeUpdate();
            LOGGER.log(Level.INFO, "Product updated successfully");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating product:", e);
        }
    }
    private void setProduct(String name, String category, BigDecimal price, String product_type, String storage_conditions, BigDecimal weight, String shelf_life, String ingredients, BigDecimal kcal_per_100g, BigDecimal kj_per_100g, BigDecimal fats, BigDecimal saturated_fats, BigDecimal carbohydrates, BigDecimal sugars, BigDecimal salt, BigDecimal fiber, BigDecimal proteins, PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, name);
        pstmt.setString(2, category);
        pstmt.setBigDecimal(3, price);
        pstmt.setString(4, product_type);
        pstmt.setString(5, storage_conditions);
        pstmt.setBigDecimal(6, weight);
        pstmt.setString(7, shelf_life);
        pstmt.setString(8, ingredients);
        pstmt.setBigDecimal(9, kcal_per_100g);
        pstmt.setBigDecimal(10, kj_per_100g);
        pstmt.setBigDecimal(11, fats);
        pstmt.setBigDecimal(12, saturated_fats);
        pstmt.setBigDecimal(13, carbohydrates);
        pstmt.setBigDecimal(14, sugars);
        pstmt.setBigDecimal(15, salt);
        pstmt.setBigDecimal(16, fiber);
        pstmt.setBigDecimal(17, proteins);
    }

}
