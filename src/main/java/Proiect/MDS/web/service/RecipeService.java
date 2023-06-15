package Proiect.MDS.web.service;

import Proiect.MDS.web.dto.RecipeDto;
import Proiect.MDS.web.models.Recipe;

import java.util.List;

public interface RecipeService {
    Recipe createRecipe(RecipeDto recipeDto);

    Recipe getRecipeById(int id);

    List<RecipeDto> getAllRecipes();

    void deleteRecipe(int id);
}
