package Proiect.MDS.web.service;

import Proiect.MDS.web.dto.RecipeDto;
import Proiect.MDS.web.models.Recipe;

import java.util.List;

/**
 * Interface for a service that manages Recipe instances.
 */
public interface RecipeService {
    /**
     * Creates a new Recipe instance based on a RecipeDto.
     *
     * @param recipeDto RecipeDto instance containing the data for the new Recipe.
     * @return The created Recipe instance.
     */
    Recipe createRecipe(RecipeDto recipeDto);

    /**
     * Retrieves a Recipe instance by its ID.
     *
     * @param id The ID of the Recipe to retrieve.
     * @return The found Recipe instance.
     */
    Recipe getRecipeById(int id);

    /**
     * Retrieves all Recipe instances.
     *
     * @return A list of all RecipeDto instances.
     */
    List<RecipeDto> getAllRecipes();

    /**
     * Deletes a Recipe instance by its ID.
     *
     * @param id The ID of the Recipe to delete.
     */
    void deleteRecipe(int id);
}
