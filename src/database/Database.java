package database;

import DataModel.Product;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Database class serves to connect to the database, create tables, insert and update products, and close the connection to the database.
 */
public class Database {
    // Logger object to log the database activities and any errors that might occur
    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());

    // Connection object to establish the connection to the database
    private Connection connection;
    private Statement stmt;

    /**
     * Constructor of the Database class which connects to the database using provided URL, username and password.
     *
     * @param url      the URL of the database to connect to
     * @param username the username of the database
     * @param password the password of the database
     */
    public Database(String url, String username, String password) {
        try {
            connection = DriverManager.getConnection(url, username, password);
            stmt = connection.createStatement();

            LOGGER.log(Level.INFO, "Connected to the MySQL database!");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error connecting to the MySQL database:", e);
        }
    }

    /**
     * Closes the database connection and the statement.
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
            LOGGER.log(Level.SEVERE, "Error closing the MySQL database connection:", e);
        }
    }


    /**
     * Creates a new table in the database called 'products'.
     */
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

    /**
     * Inserts a new product into the 'products' table.
     *
     * @param name               the name of the product
     * @param category           the category of the product
     * @param price              the price of the product
     * @param product_type       the type of the product
     * @param storage_conditions the storage conditions of the product
     * @param weight             the weight of the product
     * @param shelf_life         the shelf life of the product
     * @param ingredients        the ingredients of the product
     * @param kcal_per_100g      the kcal_per_100g of the product
     * @param kj_per_100g        the kj_per_100g of the product
     * @param fats               the fats of the product
     * @param saturated_fats     the saturated fats of the product
     * @param carbohydrates      the carbohydrates of the product
     * @param sugars             the sugars of the product
     * @param salt               the salt of the product
     * @param fiber              the fiber of the product
     * @param proteins           the proteins of the product
     */
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

    /**
     * Updates an existing product in the 'products' table by its id.
     *
     * @param id                 the id of the product
     * @param name               the name of the product
     * @param category           the category of the product
     * @param price              the price of the product
     * @param product_type       the type of the product
     * @param storage_conditions the storage conditions of the product
     * @param weight             the weight of the product
     * @param shelf_life         the shelf life of the product
     * @param ingredients        the ingredients of the product
     * @param kcal_per_100g      the kcal_per_100g of the product
     * @param kj_per_100g        the kj_per_100g of the product
     * @param fats               the fats of the product
     * @param saturated_fats     the saturated fats of the product
     * @param carbohydrates      the carbohydrates of the product
     * @param sugars             the sugars of the product
     * @param salt               the salt of the product
     * @param fiber              the fiber of the product
     * @param proteins           the proteins of the product
     */
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

    /**
     * This private method is used to set the parameters of the PreparedStatement used in 'insertProduct' and 'updateProduct' methods.
     *
     * @param name               the name of the product
     * @param category           the category of the product
     * @param price              the price of the product
     * @param product_type       the type of the product
     * @param storage_conditions the storage conditions of the product
     * @param weight             the weight of the product
     * @param shelf_life         the shelf life of the product
     * @param ingredients        the ingredients of the product
     * @param kcal_per_100g      the kilocalories per 100 grams of the product
     * @param kj_per_100g        the kilojoules per 100 grams of the product
     * @param fats               the fats of the product
     * @param saturated_fats     the saturated fats of the product
     * @param carbohydrates      the carbohydrates of the product
     * @param sugars             the sugars of the product
     * @param salt               the salt of the product
     * @param fiber              the fiber of the product
     * @param proteins           the proteins of the product
     * @param pstmt              the PreparedStatement to set the parameters on
     * @throws SQLException If there is an error setting the PreparedStatement parameters.
     */
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

    /**
     * Retrieves a product from the 'products' table by its name.
     *
     * @param name the name of the product
     * @return the Product object that matches the provided name
     */
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

    /**
     * Updates the last modification time of a product in the 'products' table by its id.
     *
     * @param id the id of the product
     */
    public void updateLastModificationTime(int id) {
        String query = "UPDATE products SET last_modified = NOW() WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating last modification time:", e);
        }
    }


    /**
     * Deletes a product from the 'products' table by its id.
     *
     * @param id the id of the product
     */
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

    /**
     * Retrieves all products from the 'products' table.
     *
     * @return a list of all Product objects in the 'products' table
     */
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

    /**
     * Converts a ResultSet to a Product object.
     *
     * @param rs the ResultSet to convert
     * @return the Product object converted from the ResultSet
     * @throws SQLException If there is an error reading the ResultSet.
     */
    private Product createProductFromResultSet(ResultSet rs) throws SQLException {
        return getProduct(rs);
    }

    /**
     * Converts a ResultSet to a Product object.
     *
     * @param rs the ResultSet to convert
     * @return the Product object converted from the ResultSet
     * @throws SQLException If there is an error reading the ResultSet.
     */
    private Product getProduct(ResultSet rs) throws SQLException {
        return new Product(rs.getInt("id"), rs.getString("name"), rs.getString("category"),
                rs.getBigDecimal("price"), rs.getString("product_type"), rs.getString("storage_conditions"),
                rs.getBigDecimal("weight"), rs.getString("shelf_life"), rs.getString("ingredients"),
                rs.getBigDecimal("kcal_per_100g"), rs.getBigDecimal("kj_per_100g"), rs.getBigDecimal("fats"),
                rs.getBigDecimal("saturated_fats"), rs.getBigDecimal("carbohydrates"), rs.getBigDecimal("sugars"),
                rs.getBigDecimal("salt"), rs.getBigDecimal("fiber"), rs.getBigDecimal("proteins"), rs.getTimestamp("last_modified").toLocalDateTime());
    }

    /**
     * Adds products to the given list of Product objects from the ResultSet.
     *
     * @param products the list of Product objects to add to
     * @param rs       the ResultSet to convert to Product objects
     * @throws SQLException If there is an error reading the ResultSet.
     */
    private void addProduct(List<Product> products, ResultSet rs) throws SQLException {
        while (rs.next()) {
            products.add(createProductFromResultSet(rs));
        }
    }

    /**
     * Retrieves products that match the provided filters. If a filter parameter is null, it is not included in the query.
     *
     * @param category     The category to filter by.
     * @param minWeight    The minimum weight to filter by.
     * @param maxPrice     The maximum price to filter by.
     * @param minShelfLife The minimum shelf life to filter by.
     * @return A list of products that match the filters.
     * @throws SQLException If there is an error executing the SQL query.
     */
    public List<Product> getProductsByFilter(String category, BigDecimal minWeight, BigDecimal maxPrice, String minShelfLife) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE 1 = 1";

        if (category != null) {
            sql += " AND category = ?";
        }
        if (minWeight != null) {
            sql += " AND weight >= ?";
        }
        if (maxPrice != null) {
            sql += " AND price <= ?";
        }
        if (minShelfLife != null) {
            sql += " AND shelf_life >= ?";
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            int paramIndex = 1;
            if (category != null) {
                stmt.setString(paramIndex++, category);
            }
            if (minWeight != null) {
                stmt.setBigDecimal(paramIndex++, minWeight);
            }
            if (maxPrice != null) {
                stmt.setBigDecimal(paramIndex++, maxPrice);
            }
            if (minShelfLife != null) {
                stmt.setString(paramIndex++, minShelfLife);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(resultSetToProduct(rs));
            }
        }

        return products;
    }

    /**
     * Helper method to convert a ResultSet to a Product object.
     *
     * @param rs The ResultSet to convert.
     * @return The Product object.
     * @throws SQLException If there is an error reading the ResultSet.
     */
    private Product resultSetToProduct(ResultSet rs) throws SQLException {

        LocalDateTime lastModified = rs.getObject("last_modified", LocalDateTime.class);
        product.setLastModified(lastModified);

        return product;
    }
}
