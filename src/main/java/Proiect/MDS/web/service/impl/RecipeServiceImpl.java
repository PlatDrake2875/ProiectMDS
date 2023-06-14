package Proiect.MDS.web.service.impl;

import Proiect.MDS.web.dto.RecipeDto;
import Proiect.MDS.web.models.Recipe;
import Proiect.MDS.web.repository.RecipeRepository;
import Proiect.MDS.web.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository repository;
    public RecipeServiceImpl(RecipeRepository repository) {
        this.repository = repository;
    }

    private RecipeDto RecipeToDto(Recipe recipe) {
        RecipeDto recipeDto = RecipeDto.builder()
                .id(recipe.getId())
                .photoURL(recipe.getPhotoURL())
                .recipeName(recipe.getRecipeName())
                .portionSize(recipe.getPortionSize())
                .estimatedCookingTime(recipe.getEstimatedCookingTime())
                .estimatedPreparationTime(recipe.getEstimatedPreparationTime())
                .build();

        return recipeDto;
    }

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

    @Override
    public Recipe getRecipeById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<RecipeDto> getAllRecipes() {
        List<Recipe> recipes = repository.findAll();
        return recipes.stream().map((recipe) -> RecipeToDto(recipe)).collect(Collectors.toList());

    }

    @Override
    public void deleteRecipe(int id) {
        repository.deleteById(id);
    }

}
