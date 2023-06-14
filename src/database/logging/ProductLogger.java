package database.logging;

import DataModel.Product;
import shopScraping.AppLogger;

import java.sql.SQLException;
import java.util.logging.Level;

/**
 * Provides logging capabilities for operations related to product database actions.
 * Extends the AppLogger class to add specific logging methods for product actions.
 */
public class ProductLogger extends AppLogger {

    /**
     * Constructs a new instance of ProductLogger.
     * @param className The class where the logger is being instantiated.
     */
    public ProductLogger(Class<?> className) {
        super(className);
    }

    /**
     * Logs a successful product insert operation.
     * @param product The product that was successfully inserted.
     */
    public void logInsert(Product product) {
        logProductOperation("Product inserted successfully: ", product);
    }

    /**
     * Logs a successful product update operation.
     * @param product The product that was successfully updated.
     */
    public void logUpdate(Product product) {
        logProductOperation("Product updated successfully: ", product);
    }

    /**
     * Logs an error encountered during product insert operation.
     * @param product The product whose insertion caused the error.
     * @param e The SQLException encountered during the operation.
     */
    public void logInsertError(Product product, SQLException e) {
        logProductOperationError("Error inserting product: ", product, e);
    }

    /**
     * Logs an error encountered during product update operation.
     * @param product The product whose update caused the error.
     * @param e The SQLException encountered during the operation.
     */
    public void logUpdateError(Product product, SQLException e) {
        logProductOperationError("Error updating product: ", product, e);
    }

    /**
     * Logs an error encountered during the execution of a query.
     * @param query The query whose execution caused the error.
     * @param e The SQLException encountered during the query execution.
     */
    public void logQueryError(String query, SQLException e) {
        getLogger().log(Level.SEVERE, e, () -> "Error executing query: " + query);
    }

    /**
     * Logs an error encountered during product retrieval operation.
     * @param operation The operation during which the product retrieval error occurred.
     * @param e The SQLException encountered during the product retrieval operation.
     */
    public void logRetrieveError(String operation, SQLException e) {
        getLogger().log(Level.SEVERE, e, () -> "Error retrieving product during " + operation + ": ");
    }

    /**
     * Helper method to log successful product operations.
     * @param logMessage The message to be logged.
     * @param product The product associated with the operation.
     */
    private void logProductOperation(String logMessage, Product product) {
        getLogger().log(Level.INFO, () -> logMessage + product);
    }

    /**
     * Helper method to log errors encountered during product operations.
     * @param logMessage The message to be logged.
     * @param product The product associated with the operation.
     * @param e The SQLException encountered during the operation.
     */
    private void logProductOperationError(String logMessage, Product product, SQLException e) {
        getLogger().log(Level.SEVERE, e, () -> logMessage + product);
    }
}
