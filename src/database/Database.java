package database;

import DataModel.Product;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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

    public Product getProductByName(String name) {
        String query = "SELECT * FROM products WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return getProduct(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting product by name:", e);
        }
        return null;
    }

    public void updateLastModificationTime(int id) {
        String query = "UPDATE products SET last_modified = NOW() WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating last modification time:", e);
        }
    }


    public void deleteProduct(int id) {
        String query = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            LOGGER.log(Level.INFO, "Product deleted successfully");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting product:", e);
        }
    }

    public List<Product> getAllProducts() {
        String query = "SELECT * FROM products";
        List<Product> products = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            addProduct(products, rs);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all products:", e);
        }
        return products;
    }

    public List<Product> getProductsByCategory(String category) {
        String query = "SELECT * FROM products WHERE category = ?";
        List<Product> products = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();

            addProduct(products, rs);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting products by category:", e);
        }
        return products;
    }

    private Product createProductFromResultSet(ResultSet rs) throws SQLException {
        return getProduct(rs);
    }

    private Product getProduct(ResultSet rs) throws SQLException {
        return new Product(rs.getInt("id"), rs.getString("name"), rs.getString("category"),
                rs.getBigDecimal("price"), rs.getString("product_type"), rs.getString("storage_conditions"),
                rs.getBigDecimal("weight"), rs.getString("shelf_life"), rs.getString("ingredients"),
                rs.getBigDecimal("kcal_per_100g"), rs.getBigDecimal("kj_per_100g"), rs.getBigDecimal("fats"),
                rs.getBigDecimal("saturated_fats"), rs.getBigDecimal("carbohydrates"), rs.getBigDecimal("sugars"),
                rs.getBigDecimal("salt"), rs.getBigDecimal("fiber"), rs.getBigDecimal("proteins"));
    }

    private void addProduct(List<Product> products, ResultSet rs) throws SQLException {
        while (rs.next()) {
            products.add(createProductFromResultSet(rs));
        }
    }


    public List<Product> getProductsByCategoryAndPrice(String category, BigDecimal price) {
        String query = "SELECT * FROM products WHERE category = ? AND price <= ?";
        List<Product> products = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, category);
            pstmt.setBigDecimal(2, price);
            ResultSet rs = pstmt.executeQuery();

            addProduct(products, rs);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting products by category and price:", e);
        }
        return products;
    }

    public List<Product> getProductsByCategoryAndPriceAndWeight(String category, BigDecimal price, BigDecimal weight) {
        String query = "SELECT * FROM products WHERE category = ? AND price <= ? AND weight <= ?";
        List<Product> products = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, category);
            pstmt.setBigDecimal(2, price);
            pstmt.setBigDecimal(3, weight);
            ResultSet rs = pstmt.executeQuery();

            addProduct(products, rs);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting products by category and price and weight:", e);
        }
        return products;

    }

    public List<Product> getProductsByCategoryAndWeight(String category, BigDecimal weight) {
        String query = "SELECT * FROM products WHERE category = ? AND weight <= ?";
        List<Product> products = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, category);
            pstmt.setBigDecimal(2, weight);
            ResultSet rs = pstmt.executeQuery();

            addProduct(products, rs);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting products by category and weight:", e);
        }
        return products;
    }

    public List<Product> getProductsByCategoryAndPriceAndWeightAndShelfLife(String category, BigDecimal price, BigDecimal weight, String shelfLife) {
        String query = "SELECT * FROM products WHERE category = ? AND price <= ? AND weight <= ? AND shelf_life <= ?";
        List<Product> products = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, category);
            pstmt.setBigDecimal(2, price);
            pstmt.setBigDecimal(3, weight);
            pstmt.setString(4, shelfLife);
            ResultSet rs = pstmt.executeQuery();

            addProduct(products, rs);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting products by category and price and weight and shelf life:", e);
        }
        return products;
    }

}
