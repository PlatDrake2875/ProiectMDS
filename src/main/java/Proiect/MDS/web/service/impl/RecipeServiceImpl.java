package Proiect.MDS.web.service.impl;

import Proiect.MDS.web.dto.RecipeDto;
import Proiect.MDS.web.models.Recipe;
import Proiect.MDS.web.repository.RecipeRepository;
import Proiect.MDS.web.service.RecipeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the RecipeService interface.
 * It uses a RecipeRepository to perform CRUD operations.
 */
@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository repository;

    /**
     * Constructor with dependency injection via constructor
     * @param repository The RecipeRepository to inject.
     */
    public RecipeServiceImpl(RecipeRepository repository) {
        this.repository = repository;
    }

    /**
     * Converts a Recipe model object into a Recipe DTO object.
     * @param recipe The Recipe object to convert.
     * @return The converted RecipeDto object.
     */
    public RecipeDto RecipeToDto(Recipe recipe) {
        return RecipeDto.builder()
                .id(recipe.getId())
                .photoURL(recipe.getPhotoURL())
                .recipeName(recipe.getRecipeName())
                .portionSize(recipe.getPortionSize())
                .estimatedCookingTime(recipe.getEstimatedCookingTime())
                .estimatedPreparationTime(recipe.getEstimatedPreparationTime())
                .build();
    }

    /**
     * Creates a Recipe model from a RecipeDto and saves it in the repository.
     * @param recipeDto The RecipeDto object to convert and save.
     * @return The saved Recipe object.
     */
    @Override
    public Recipe createRecipe(RecipeDto recipeDto) {
        Recipe recipe = new Recipe(recipeDto.getRecipeName(),
                recipeDto.getPhotoURL(),
                recipeDto.getEstimatedCookingTime(),
                recipeDto.getEstimatedPreparationTime(),
                recipeDto.getPortionSize(),
                recipeDto.getProducts());

        return repository.save(recipe);
    }

    /**
     * Retrieves a Recipe by its ID from the repository.
     * @param id The ID of the Recipe to retrieve.
     * @return The found Recipe, or null if no Recipe with the provided ID could be found.
     */
    @Override
    public Recipe getRecipeById(int id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * Retrieves all Recipes from the repository and converts them to RecipeDto objects.
     * @return A list of RecipeDto objects.
     */
    @Override
    public List<RecipeDto> getAllRecipes() {
        return repository.findAll().stream().map(this::RecipeToDto).toList();
    }

    /**
     * Deletes a Recipe by its ID from the repository.
     * @param id The ID of the Recipe to delete.
     */
    @Override
    public void deleteRecipe(int id) {
        repository.deleteById(id);
    }
}
