package Proiect.MDS.web.controller;

import Proiect.MDS.web.dto.RecipeDto;
import Proiect.MDS.web.models.Recipe;
import Proiect.MDS.web.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controller for managing Recipe related HTTP requests.
 * This class uses RecipeService to handle its operations.
 */
@Controller
public class RecipeController {
    private final RecipeService service;

    /**
     * Constructor with dependency injection via constructor
     * @param service The RecipeService to inject.
     */
    public RecipeController(RecipeService service) {
        this.service = service;
    }

    /**
     * Endpoint that returns all recipes in a list view.
     * @param model The Model object used to bind data to the view.
     * @return The name of the template to be rendered.
     */
    @GetMapping("/recipes")
    public String listRecipes(Model model) {
        List<RecipeDto> recipes = service.getAllRecipes();
        model.addAttribute("recipes", recipes);
        return "recipes-list";
    }

    /**
     * Endpoint that returns a specific recipe by id.
     * @param model The Model object used to bind data to the view.
     * @param id The id of the recipe.
     * @return The name of the template to be rendered, or an appropriate HTTP error status if the recipe is not found.
     */
    @GetMapping("/recipe-id/{id}")
    public String getById(Model model, @PathVariable(name = "id") Integer id) {
        Recipe recipe = service.getRecipeById(id);

        if (recipe == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found");
        }

        model.addAttribute("recipe", recipe);
        // String[] products = recipe.getProducts().split(" ");
        // model.addAttribute("products", products);
        return "recipe-id";
    }
}
