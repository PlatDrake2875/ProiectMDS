package database.logging;

import shopScraping.AppLogger;

import java.sql.SQLException;
import java.util.logging.Level;

/**
 * Logger specifically designed for handling logging related to database operations.
 */
public class DatabaseLogger extends AppLogger {

    /**
     * Constructor that calls the parent's constructor.
     * @param className The name of the class that the logger is being created for.
     */
    public DatabaseLogger(Class<?> className) {
        super(className);
    }

    /**
     * Logs a database connection error.
     *
     * @param e The exception related to the connection error.
     */
    public void logDatabaseConnectionError(SQLException e) {
        getLogger().log(Level.SEVERE, "Error connecting to the database", e);
    }

    /**
     * Logs a database disconnection error.
     *
     * @param e The exception related to the disconnection error.
     */
    public void logDatabaseDisconnectionError(SQLException e) {
        getLogger().log(Level.SEVERE, "Error disconnecting from the database", e);
    }

    /**
     * Logs a successful database operation.
     *
     * @param operation The operation that was successfully executed.
     */
    public void logSuccessfulOperation(String operation) {
        getLogger().log(Level.INFO, "Successfully executed database operation: {0}", operation);
    }

    /**
     * Logs a failed database operation.
     *
     * @param operation The operation that failed.
     * @param e         The exception related to the failed operation.
     */
    public void logFailedOperation(String operation, SQLException e) {
        getLogger().log(Level.SEVERE, "Failed to execute database operation: {0}", operation);
    }
}
