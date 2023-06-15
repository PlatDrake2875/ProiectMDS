package Proiect.MDS.web.database.logging;


import Proiect.MDS.web.models.Recipe;
import Proiect.MDS.web.shopScraping.AppLogger;

import java.sql.SQLException;
import java.util.logging.Level;

public class RecipeLogger extends AppLogger {

    public RecipeLogger(Class<?> className) {
        super(className);
    }

    public void logInsert(Recipe recipe) {
        logProductOperation("Recipe inserted successfully: ", recipe);
    }

    public void logUpdate(Recipe recipe) {
        logProductOperation("Recipe updated successfully: ", recipe);
    }

    public void logInsertError(Recipe recipe, SQLException e) {
        logProductOperationError("Error inserting recipe: ", recipe, e);
    }

    public void logUpdateError(Recipe recipe, SQLException e) {
        logProductOperationError("Error updating recipe: ", recipe, e);
    }

    public void logQueryError(String query, SQLException e) {
        getLogger().log(Level.SEVERE, e, () -> "Error executing query: " + query);
    }

    public void logRetrieveError(String operation, SQLException e) {
        getLogger().log(Level.SEVERE, e, () -> "Error retrieving product during " + operation + ": ");
    }

    private void logProductOperation(String logMessage, Recipe product) {
        getLogger().log(Level.INFO, () -> logMessage + product);
    }

    private void logProductOperationError(String logMessage, Recipe recipe, SQLException e) {
        getLogger().log(Level.SEVERE, e, () -> logMessage + recipe);
    }

    public void logRecipe(Recipe recipe) {
        getLogger().log(Level.INFO, () -> "Recipe: " + recipe);
    }
}