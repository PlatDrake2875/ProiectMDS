package database.logging;

import DataModel.Product;
import shopScraping.AppLogger;

import java.sql.SQLException;
import java.util.logging.Level;

public class ProductLogger extends AppLogger {

    public ProductLogger(Class<?> className) {
        super(className);
    }

    public void logInsert(Product product) {
        logProductOperation("Product inserted successfully: ", product);
    }

    public void logUpdate(Product product) {
        logProductOperation("Product updated successfully: ", product);
    }

    public void logInsertError(Product product, SQLException e) {
        logProductOperationError("Error inserting product: ", product, e);
    }

    public void logUpdateError(Product product, SQLException e) {
        logProductOperationError("Error updating product: ", product, e);
    }

    public void logQueryError(String query, SQLException e) {
        getLogger().log(Level.SEVERE, e, () -> "Error executing query: " + query);
    }

    public void logRetrieveError(String operation, SQLException e) {
        getLogger().log(Level.SEVERE, e, () -> "Error retrieving product during " + operation + ": ");
    }

    private void logProductOperation(String logMessage, Product product) {
        getLogger().log(Level.INFO, () -> logMessage + product);
    }

    private void logProductOperationError(String logMessage, Product product, SQLException e) {
        getLogger().log(Level.SEVERE, e, () -> logMessage + product);
    }
}
