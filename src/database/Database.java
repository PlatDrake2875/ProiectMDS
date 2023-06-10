package database;

import DataModel.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Database class serves to connect to the database, create tables, insert and update products, and close the connection to the database.
 */
public class Database {
    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());
    private static final String CREATE_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS products (
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
    private static final String INSERT_SQL = """
            INSERT INTO products (name, category, price,
            product_type, storage_conditions, weight,
            shelf_life, ingredients, kcal_per_100g,
            kj_per_100g, fats, saturated_fats,
            carbohydrates, sugars, salt,
            fiber, proteins, last_modified) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW());
            """;
    private static final String UPDATE_SQL = """
            UPDATE products SET
            name = ?, category = ?, price = ?,
            product_type = ?, storage_conditions = ?, weight = ?,
            shelf_life = ?, ingredients = ?, kcal_per_100g = ?,
            kj_per_100g = ?, fats = ?, saturated_fats = ?,
            carbohydrates = ?, sugars = ?, salt = ?,
            fiber = ?, proteins = ?, last_modified = NOW() WHERE id = ?;
            """;

    static {
        try {
            FileHandler fh = new FileHandler("CrawlerLogs.log", true);  // Set true to append.

            // Remove the default console handler.
            for (Handler h : LOGGER.getHandlers()) {
                LOGGER.removeHandler(h);
            }

            LOGGER.addHandler(fh);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

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
            stmt.execute(CREATE_TABLE_SQL);

            LOGGER.log(Level.INFO, "Connected to the MySQL database and table 'products' created successfully");
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
     * This method is used to insert a new Product into the 'products' database.
     * The Product object is parsed and its data is prepared and executed in an SQL INSERT statement.
     *
     * @param product The Product object containing the product's details that needs to be added into the database.
     *                <p>
     *                Usage:
     *                Product newProduct = new Product("Product Name", "Category", new BigDecimal("10.00"), ...);
     *                Database db = new Database();
     *                db.insertProduct(newProduct); // Inserts the newProduct object into the database.
     *                <p>
     *                Note:
     *                1. It prepares an SQL INSERT statement with the help of a PreparedStatement to prevent SQL injection attacks.
     *                2. The data of the Product object is set in the SQL statement using the setProduct() method.
     *                3. After the execution of the SQL statement, a log message is written indicating the successful insertion of the product.
     *                4. If an SQLException occurs during the execution of the SQL statement, it is caught and logged at a severe level.
     * @throws SQLException If there is an error with SQL handling, it throws an SQLException. It's recommended to use proper exception handling while using this method.
     */
    public void insertProduct(Product product) {
        executeUpdate(INSERT_SQL, product, false);
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
        executeUpdate(UPDATE_SQL, product, true);
    }

    /**
     * Executes an INSERT or UPDATE operation on the 'products' table.
     *
     * @param sql      The SQL string for the operation. Requires placeholders ('?') for parameters.
     * @param product  The Product object with details to be inserted/updated.
     * @param isUpdate A boolean flag indicating if the operation is an UPDATE (true) or INSERT (false).
     */
    private void executeUpdate(String sql, Product product, boolean isUpdate) {
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            setProduct(product, pstmt, isUpdate);
            pstmt.executeUpdate();

            String operation = isUpdate ? "updated" : "inserted";
            LOGGER.log(Level.INFO, "Product " + operation + " successfully");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error " + (isUpdate ? "updating" : "inserting") + " product:", e);
        }
    }

    /**
     * This method updates the last_modified field for a specified product in the database.
     * The last_modified field is updated to the current time.
     *
     * @param id The id of the product for which the last_modified time is to be updated. <p>
     *           <p>
     *           Usage:
     *           Database db = new Database();
     *           db.updateLastModificationTime(1); // Updates the last modification time for the product with id 1.
     *           <p>
     *           Note: <p>
     *           1. This method uses the SQL NOW() function to get the current time. The time is based on the system clock of the database server. <p>
     *           2. It prepares a SQL statement with the help of a PreparedStatement to prevent SQL injection attacks. <p>
     *           3. If an SQLException occurs during the execution of the SQL statement, it is caught and logged at a severe level. <p>
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
     * <p>
     * Note:
     * 1. Each field of the Product object is set by extracting the respective field's data from the ResultSet using the appropriate getter method (getInt, getString, getBigDecimal, etc.).
     * 2. This method expects the ResultSet to have all fields that are used in the Product object. If the ResultSet does not have a required field, it may throw an SQLException.
     * 3. The last modification time is converted to a LocalDateTime object before being set in the Product object.
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
    private void setProduct(Product product, PreparedStatement pstmt, boolean isUpdate) throws SQLException {
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
        if (isUpdate) {
            pstmt.setInt(18, product.getId());
        }
    }

    /**
     * Prints all the products in the database.
     */
    public void printAllProducts() {
        String query = "SELECT * FROM Products";
        executeAndPrintQuery(query);
    }

    /**
     * Prints the products that match a specific criteria.
     * The criteria is specified as a SQL WHERE clause.
     *
     * @param whereClause The SQL WHERE clause specifying the criteria.
     */
    public void printProductsByCriteria(String whereClause) {
        String query = "SELECT * FROM Products WHERE " + whereClause;
        executeAndPrintQuery(query);
    }

    private void executeAndPrintQuery(String query) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Product product = buildProduct(rs);
                System.out.println(product.toString());
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error executing and printing query:", e);
        }
    }

    /**
     * Returns a Product with a given name.
     *
     * @param name Name of the Product.
     * @return a Product with that name.
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
            LOGGER.log(Level.SEVERE, "Error retrieving product by name:", e);
        }
        return null;
    }

    /**
     * This method fetches all products from the database and exports them to a JSON file.
     *
     * @param filename Name of the JSON file where the data will be exported.
     */
    public void exportProductsToJson(String filename) {
        List<Product> products = getAllProducts();
        if (products.isEmpty()) {
            LOGGER.log(Level.INFO, "No products to export");
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        // Configure the mapper to produce pretty-printed JSON
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            // Serialize the list of products to a JSON file
            mapper.writeValue(Paths.get(filename).toFile(), products);
            LOGGER.log(Level.INFO, "Products exported successfully to " + filename);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error exporting products to JSON:", e);
        }
    }

    /**
     * This method fetches all products from the database.
     *
     * @return a List of Product objects
     */
    private List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                products.add(buildProduct(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all products:", e);
        }
        return products;
    }

    /**
     * Imports products from a JSON file and inserts/updates them to the database.
     *
     * @param filename the name of the JSON file
     */
    public void importProductsFromJson(String filename) {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(filename);

        try {
            List<Product> products = mapper.readValue(file, new TypeReference<List<Product>>() {
            });

            for (Product product : products) {
                Product existingProduct = getProductByName(product.getName());

                if (existingProduct == null) {
                    insertProduct(product);
                } else {
                    product.setId(existingProduct.getId());
                    updateProduct(product);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading products from JSON file:", e);
        }
    }

}
