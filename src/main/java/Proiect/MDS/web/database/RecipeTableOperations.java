package Proiect.MDS.web.database;

import Proiect.MDS.web.database.logging.RecipeLogger;
import Proiect.MDS.web.models.Recipe;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles operations related to the recipe table in the database.
 */
public class RecipeTableOperations {
    protected static final String CREATE_TABLE_SQL = """
        CREATE TABLE IF NOT EXISTS recipes (
            id INT AUTO_INCREMENT PRIMARY KEY,
            photourl VARCHAR(255),
            recipe_name VARCHAR(255) NOT NULL,
            estimated_cooking_time INT,
            estimated_preparation_time INT,
            portion_size INT,
            products TEXT
        );
        """;

    private static final RecipeLogger LOGGER = new RecipeLogger(RecipeTableOperations.class);
    private static final String INSERT_SQL = """
    INSERT INTO recipes (photourl, recipe_name, estimated_cooking_time,
    estimated_preparation_time, portion_size, products) VALUES (?, ?, ?, ?, ?, ?);
    """;

    private static final String UPDATE_SQL = """
        UPDATE recipes SET
        photourl = ?, recipe_name = ?, estimated_cooking_time = ?,
        estimated_preparation_time = ?, portion_size = ?, products = ? WHERE id = ?;
        """;

    private final Database database;

    /**
     * Constructor initializes the database connection.
     *
     * @param database Database object.
     */
    public RecipeTableOperations(Database database) {
        this.database = database;
    }

    /**
     * Inserts a new recipe into the recipes table.
     *
     * @param recipe The recipe to be inserted.
     */
    public void insertRecipe(Recipe recipe) {
        try {
            executeUpdate(INSERT_SQL, recipe, false);
            System.out.println("Recipe inserted successfully!");
            LOGGER.logInsert(recipe);
        } catch (SQLException e) {
            LOGGER.logInsertError(recipe, e);
        }
    }

    /**
     * Updates an existing recipe in the recipes table.
     *
     * @param recipe The recipe to be updated.
     */
    public void updateRecipe(Recipe recipe) {
        try {
            executeUpdate(UPDATE_SQL, recipe, true);
            LOGGER.logUpdate(recipe);
        } catch (SQLException e) {
            LOGGER.logUpdateError(recipe, e);
        }
    }

    /**
     * Executes a SQL update query (insert or update).
     * This method uses a PreparedStatement to execute the SQL query.
     *
     * @param sql      The SQL query to be executed.
     * @param recipe  The recipe for which the query will be executed.
     * @param isUpdate A flag indicating whether the query is an update operation.
     * @throws SQLException If an error occurs while executing the SQL query.
     */
    private void executeUpdate(String sql, Recipe recipe, boolean isUpdate) throws SQLException {
        try (PreparedStatement pstmt = database.connection.prepareStatement(sql)) {
            int index = 1;
            pstmt.setString(index++, recipe.getPhotoURL());
            pstmt.setString(index++, recipe.getRecipeName());
            pstmt.setInt(index++, recipe.getEstimatedCookingTime());
            pstmt.setInt(index++, recipe.getEstimatedPreparationTime());
            pstmt.setInt(index++, recipe.getPortionSize());
            pstmt.setString(index++, recipe.getProducts());

            if (isUpdate) {
                pstmt.setInt(index, recipe.getId());
            }

            pstmt.executeUpdate();
        }
    }

    /**
     * Prints all recipes from the recipes table.
     */
    public void printAllRecipes() {
        String query = "SELECT * FROM recipes";
        executeAndPrintQuery(query);
    }

    /**
     * Prints recipes from the recipes table that match the given criteria.
     *
     * @param column The column used for the criteria.
     * @param value  The value used for the criteria.
     */
    public void printRecipesByCriteria(String column, String value) {
        String query = String.format("SELECT * FROM recipes WHERE %s = '%s'", column, value);
        executeAndPrintQuery(query);
    }

    /**
     * Executes a SQL select query and prints the results.
     *
     * @param query The SQL query to be executed.
     */
    private void executeAndPrintQuery(String query) {
        try (Statement stmt = database.connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Recipe recipe = Recipe.buildRecipe(rs);
                LOGGER.logRecipe(recipe);
            }
        } catch (SQLException e) {
            LOGGER.logQueryError(query, e);
        }
    }

    /**
     * Gets a recipe from the recipes table by name.
     *
     * @param name The name of the recipe.
     * @return The recipe, if found; null otherwise.
     */
    public Recipe getRecipeByName(String name) {
        return getRecipeByColumnValue("recipe_name", name);
    }


    /**
     * Gets a recipe from the recipes table by the value of a specific column.
     *
     * @param column The column used for the criteria.
     * @param value  The value used for the criteria.
     * @return The recipe, if found; null otherwise.
     */
    public Recipe getRecipeByColumnValue(String column, String value) {
        String query = String.format("SELECT * FROM recipes WHERE %s = '%s'", column, value);
        try (Statement stmt = database.connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                return Recipe.buildRecipe(rs);
            }
        } catch (SQLException e) {
            LOGGER.logQueryError(query, e);
        }

        return null;
    }

    /**
     * Gets all recipes from the recipes table.
     *
     * @return A list of all recipes.
     */
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        String query = "SELECT * FROM recipes";
        try (Statement stmt = database.connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Recipe recipe = Recipe.buildRecipe(rs);
                recipes.add(recipe);
            }
        } catch (SQLException e) {
            LOGGER.logQueryError(query, e);
        }

        return recipes;
    }
}
