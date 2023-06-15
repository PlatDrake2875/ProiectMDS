package Proiect.MDS.web.database;


import Proiect.MDS.web.database.logging.ProductLogger;
import Proiect.MDS.web.models.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static Proiect.MDS.web.models.Product.buildProduct;

/**
 * This class handles operations related to the product table in the database.
 */
public class ProductTableOperations {
    protected static final String CREATE_TABLE_SQL = """
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
    private static final ProductLogger LOGGER = new ProductLogger(ProductTableOperations.class);
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
    private final Database database;


    /**
     * Constructor initializes the database connection.
     *
     * @param database Database object.
     */
    public ProductTableOperations(Database database) {
        this.database = database;
    }


    /**
     * Inserts a new product into the products table.
     * This method logs a message when a product is successfully inserted and logs an error if insertion fails.
     *
     * @param product The product to be inserted.
     */
    public void insertProduct(Product product) {
        try {
            executeUpdate(INSERT_SQL, product, false);
            LOGGER.logInsert(product);
        } catch (SQLException e) {
            LOGGER.logInsertError(product, e);
        }
    }

    /**
     * Updates an existing product in the products table.
     * This method logs a message when a product is successfully updated and logs an error if the update fails.
     *
     * @param product The product to be updated.
     */
    public void updateProduct(Product product) {
        try {
            executeUpdate(UPDATE_SQL, product, true);
            LOGGER.logUpdate(product);
        } catch (SQLException e) {
            LOGGER.logUpdateError(product, e);
        }
    }

    /**
     * Executes a SQL update query (insert or update).
     * This method uses a PreparedStatement to execute the SQL query.
     *
     * @param sql      The SQL query to be executed.
     * @param product  The product for which the query will be executed.
     * @param isUpdate A flag indicating whether the query is an update operation.
     * @throws SQLException If an error occurs while executing the SQL query.
     */
    private void executeUpdate(String sql, Product product, boolean isUpdate) throws SQLException {
        try (PreparedStatement pstmt = database.connection.prepareStatement(sql)) {
            product.setProduct(pstmt, isUpdate);
            pstmt.executeUpdate();
        }
    }

    /**
     * Prints all products from the products table.
     */
    public void printAllProducts() {
        String query = "SELECT * FROM Products";
        executeAndPrintQuery(query);
    }

    /**
     * Prints products from the products table that match the given criteria.
     *
     * @param whereClause The criteria for selecting products.
     */
    public void printProductsByCriteria(String whereClause) {
        String query = "SELECT * FROM Products WHERE " + whereClause;
        executeAndPrintQuery(query);
    }

    /**
     * Executes the provided query and prints the resulting products.
     *
     * @param query The query to be executed.
     */
    private void executeAndPrintQuery(String query) {
        try (Statement stmt = database.connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Product product = buildProduct(rs);
                System.out.println(product);
            }
        } catch (SQLException e) {
            LOGGER.logQueryError(query, e);
        }
    }

    /**
     * Returns a product that matches the given name.
     *
     * @param name The name of the product to be retrieved.
     * @return A product that matches the given name, or null if no such product exists.
     */
    public Product getProductByName(String name) {
        String query = "SELECT * FROM products WHERE name = ?";
        try (PreparedStatement pstmt = database.connection.prepareStatement(query)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return buildProduct(rs);
            }
        } catch (SQLException e) {
            LOGGER.logRetrieveError("getProductByName", e);
        }
        return null;
    }

    /**
     * Returns a product that matches the given column name and value.
     *
     * @param columnName The name of the column in the product table.
     * @param value      The value in the column to be matched.
     * @return A product that matches the given column name and value, or null if no such product exists.
     */
    public Product getProductByColumnValue(String columnName, String value) {
        String query = "SELECT * FROM products WHERE " + columnName + " = ?";
        try (PreparedStatement pstmt = database.connection.prepareStatement(query)) {
            pstmt.setString(1, value);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return buildProduct(rs);
            }
        } catch (SQLException e) {
            LOGGER.logRetrieveError("getProductByColumnValue", e);
        }
        return null;
    }

    /**
     * Retrieves all products from the products table.
     *
     * @return A list of all products in the products table.
     */
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";

        try (Statement stmt = database.connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                products.add(buildProduct(rs));
            }
        } catch (SQLException e) {
            LOGGER.logRetrieveError("getAllProducts", e);
        }
        return products;
    }

    /**
     * Returns a product that matches the given id.
     *
     * @param ingredient The name of the product to be retrieved.
     * @return A product that matches the given id, or null if no such product exists.
     */
    public Product getProductByCriteria(String ingredient) {

        String query = "SELECT * FROM products WHERE lower(name) like lower('" + ingredient + "%') ORDER BY price ASC LIMIT 1;";
        Product product = new Product();

        try(Statement stmt = database.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            while(rs.next()) {
                product = buildProduct(rs);
            }
        } catch (SQLException e) {
            LOGGER.logQueryError(query, e);
        }

        return product;
    }
}