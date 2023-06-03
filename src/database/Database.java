package database;

import DataModel.Product;

import java.math.BigDecimal;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
                    proteins DECIMAL(10, 2),
                    last_modified DATETIME
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
     * This method is used to insert a new Product into the 'products' database.
     * The Product object is parsed and its data is prepared and executed in an SQL INSERT statement.
     *
     * @param product The Product object containing the product's details that needs to be added into the database.
     *
     * Usage:
     *    Product newProduct = new Product("Product Name", "Category", new BigDecimal("10.00"), ...);
     *    Database db = new Database();
     *    db.insertProduct(newProduct); // Inserts the newProduct object into the database.
     *
     * Note:
     * 1. It prepares an SQL INSERT statement with the help of a PreparedStatement to prevent SQL injection attacks.
     * 2. The data of the Product object is set in the SQL statement using the setProduct() method.
     * 3. After the execution of the SQL statement, a log message is written indicating the successful insertion of the product.
     * 4. If an SQLException occurs during the execution of the SQL statement, it is caught and logged at a severe level.
     *
     * @throws SQLException If there is an error with SQL handling, it throws an SQLException. It's recommended to use proper exception handling while using this method.
     */
    public void insertProduct(Product product) {
        String insertSQL = """
                INSERT INTO products (name, category, price,
                product_type, storage_conditions, weight,
                shelf_life, ingredients, kcal_per_100g,
                kj_per_100g, fats, saturated_fats,
                carbohydrates, sugars, salt,
                fiber, proteins) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            setProduct(product, pstmt);

            pstmt.executeUpdate();
            LOGGER.log(Level.INFO, "Product inserted successfully");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting product:", e);
        }
    }

    /**
     * This method is used to update a product in the database.
     * It takes as input a Product object and updates the corresponding product in the database.
     * If the product is successfully updated, it logs an informational message.
     * If an SQL error occurs, it logs a severe error message.
     *
     * @param product The Product object containing the updated information of the product.
     */
    public void updateProduct(Product product) {
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
            setProduct(product, pstmt);
            pstmt.setInt(18, product.getId());

            pstmt.executeUpdate();
            LOGGER.log(Level.INFO, "Product updated successfully");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating product:", e);
        }
    }
    /**
     * This method updates the last_modified field for a specified product in the database.
     * The last_modified field is updated to the current time.
     *
     * @param id The id of the product for which the last_modified time is to be updated.
     *
     * Usage:
     *    Database db = new Database();
     *    db.updateLastModificationTime(1); // Updates the last modification time for the product with id 1.
     *
     * Note:
     * 1. This method uses the SQL NOW() function to get the current time. The time is based on the system clock of the database server.
     * 2. It prepares a SQL statement with the help of a PreparedStatement to prevent SQL injection attacks.
     * 3. If an SQLException occurs during the execution of the SQL statement, it is caught and logged at a severe level.
     *
     * @throws SQLException If there is an error with SQL handling, it throws an SQLException. It's recommended to use proper exception handling while using this method.
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
     * This method is used to update the price of a product in the database.
     * It takes as input a Product object and updates the price of the corresponding product in the database.
     * If the price is successfully updated, it logs an informational message.
     * If an SQL error occurs, it logs a severe error message.
     *
     * @param product The Product object containing the updated price of the product.
     */
    public void updateProductPrice(Product product) {
        String updateSQL = "UPDATE products SET price = ? WHERE id = ?;";

        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setBigDecimal(1, product.getPrice());
            pstmt.setInt(2, product.getId());

            pstmt.executeUpdate();
            LOGGER.log(Level.INFO, "Product price updated successfully");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating product price:", e);
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
     * This private method is used to build a Product object from a ResultSet.
     * It uses a Builder pattern to incrementally create a Product object from the database query result set.
     * This method is typically used internally by the class to handle data fetched from the database.
     *
     * @param rs The ResultSet object obtained from executing a SQL statement. It contains the product's details fetched from the database.
     * @return Returns a Product object built from the ResultSet.
     *
     * Note:
     * 1. Each field of the Product object is set by extracting the respective field's data from the ResultSet using the appropriate getter method (getInt, getString, getBigDecimal, etc.).
     * 2. This method expects the ResultSet to have all fields that are used in the Product object. If the ResultSet does not have a required field, it may throw an SQLException.
     * 3. The last modification time is converted to a LocalDateTime object before being set in the Product object.
     *
     * @throws SQLException If there is an error while handling the ResultSet, it throws an SQLException.
     *                      It's recommended to handle this exception properly while using methods that could use this method.
     */
    private Product buildProduct(ResultSet rs) throws SQLException {
        return new Product.Builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .category(rs.getString("category"))
                .price(rs.getBigDecimal("price"))
                .productType(rs.getString("product_type"))
                .storageConditions(rs.getString("storage_conditions"))
                .weight(rs.getBigDecimal("weight"))
                .shelfLife(rs.getString("shelf_life"))
                .ingredients(rs.getString("ingredients"))
                .kcalPer100g(rs.getBigDecimal("kcal_per_100g"))
                .kjPer100g(rs.getBigDecimal("kj_per_100g"))
                .fats(rs.getBigDecimal("fats"))
                .saturatedFats(rs.getBigDecimal("saturated_fats"))
                .carbohydrates(rs.getBigDecimal("carbohydrates"))
                .sugars(rs.getBigDecimal("sugars"))
                .salt(rs.getBigDecimal("salt"))
                .fiber(rs.getBigDecimal("fiber"))
                .proteins(rs.getBigDecimal("proteins"))
                .lastModified(rs.getTimestamp("last_modified").toLocalDateTime())
                .build();
    }

    /**
     * This helper method is used to set the PreparedStatement parameters from a given Product object.
     * It is used by the updateProduct method to reduce redundancy and improve code organization.
     *
     * @param product The Product object from which the PreparedStatement parameters are to be set.
     * @param pstmt   The PreparedStatement whose parameters are to be set.
     * @throws SQLException If an error occurs while setting the PreparedStatement parameters.
     */
    private void setProduct(Product product, PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, product.getName());
        pstmt.setString(2, product.getCategory());
        pstmt.setBigDecimal(3, product.getPrice());
        pstmt.setString(4, product.getProductType());
        pstmt.setString(5, product.getStorageConditions());
        pstmt.setBigDecimal(6, product.getWeight());
        pstmt.setString(7, product.getShelfLife());
        pstmt.setString(8, product.getIngredients());
        pstmt.setBigDecimal(9, product.getKcalPer100g());
        pstmt.setBigDecimal(10, product.getKjPer100g());
        pstmt.setBigDecimal(11, product.getFats());
        pstmt.setBigDecimal(12, product.getSaturatedFats());
        pstmt.setBigDecimal(13, product.getCarbohydrates());
        pstmt.setBigDecimal(14, product.getSugars());
        pstmt.setBigDecimal(15, product.getSalt());
        pstmt.setBigDecimal(16, product.getFiber());
        pstmt.setBigDecimal(17, product.getProteins());
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
                return buildProduct(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting product by name:", e);
        }
        return null;
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
            products.add(buildProduct(rs));
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
                products.add(buildProduct(rs));
            }
        }
        return products;
    }

    /**
     * This method attempts to update a product in the database, but only if it has been at least 24 hours
     * since the product was last modified. It first retrieves the last modification time from the database.
     * If 24 hours have passed, it will proceed to update the product.
     *
     * @param product The Product object that needs to be updated. The Product's id is used to fetch
     *                the current version of the product from the database.
     *
     * Note:
     * 1. The last modified timestamp is fetched from the database and converted into LocalDateTime format.
     * 2. The method uses Java's Duration class to calculate the difference between the current time and
     *    the last modification time. It uses "ECT" timezone to calculate the current time.
     * 3. If it has been less than 24 hours since the last modification, a log message is created and the update is not performed.
     * 4. If an SQLException is encountered while executing the method, an error message is logged.
     *
     * @throws SQLException If there is an error while handling the database operation, it throws an SQLException.
     *                      It's recommended to handle this exception properly when using this method.
     */
    public void updateProductIf24HoursPassed(Product product) {
        String sql = "SELECT last_modified FROM products WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            int id = product.getId();
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                LocalDateTime lastModified = rs.getTimestamp("last_modified").toLocalDateTime();
                LocalDateTime now = LocalDateTime.now(ZoneId.of("ECT"));

                if (Duration.between(lastModified, now).toHours() >= 24) {
                    updateProduct(product);
                } else {
                    LOGGER.log(Level.INFO, "It has not been 24 hours since the product was last modified");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Update check failed", e);
        }
    }
}
