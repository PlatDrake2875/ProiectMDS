package Proiect.MDS.web.controller;

import Proiect.MDS.web.database.Database;
import Proiect.MDS.web.database.ProductTableOperations;
import Proiect.MDS.web.dto.RecipeDto;
import Proiect.MDS.web.models.Product;
import Proiect.MDS.web.models.Recipe;
import Proiect.MDS.web.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
     *
     * @param service The RecipeService to inject.
     */
    public RecipeController(RecipeService service) {
        this.service = service;
    }

    /**
     * Endpoint that returns all recipes in a list view.
     *
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
     *
     * @param model The Model object used to bind data to the view.
     * @param id    The id of the recipe.
     * @return The name of the template to be rendered, or an appropriate HTTP error status if the recipe is not found.
     */
    @GetMapping("/recipe-id/{id}")
    public String getById(Model model, @PathVariable(name = "id") Integer id) {
        Recipe recipe = service.getRecipeById(id);
        model.addAttribute("recipe", recipe);
        String[] ingredients = recipe.getProducts().split(", ");

        List<Product> products = new ArrayList<>();

        for (String ingredient : ingredients) {
            ProductTableOperations pto = new ProductTableOperations(new Database("jdbc:mysql://localhost/dbProducts", "root", "2875"));
            Product newProduct = pto.getProductByCriteria(ingredient);
            products.add(newProduct);
        }

        double totalPrice = 0.0;
        for(Product product : products){
            totalPrice += product.getPrice().doubleValue();
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String formattedNumber = decimalFormat.format(totalPrice);
        double trimmedNumber = Double.parseDouble(formattedNumber);

        model.addAttribute("recipePrice", trimmedNumber);

        model.addAttribute("products", products);
        return "recipe-id";
    }


}
