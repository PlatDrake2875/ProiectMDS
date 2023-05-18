package DataModel;

import DataModel.Ingredient;
import DataModel.Product;
import java.util.List;

/**
 * The Recipe class represents a recipe with a list of products, cooking time,
 * preparation time, and other relevant information.
 */
public class Recipe {
    private String recipeName;
    private String category;
    private int estimatedCookingTime;
    private int estimatedPreparationTime;
    private int portionSize;
    private List<Ingredient> ingredients;
    private List<Product> products;

    public Recipe(String recipeName, String category, int estimatedCookingTime, int estimatedPreparationTime, int portionSize, List<Product> products, List<Ingredient> ingredients) {
        this.recipeName = recipeName;
        this.category = category;
        this.estimatedCookingTime = estimatedCookingTime;
        this.estimatedPreparationTime = estimatedPreparationTime;
        this.portionSize = portionSize;
        this.products = products;
        this.ingredients = ingredients;
    }

    // Getters and setters
    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getEstimatedCookingTime() {
        return estimatedCookingTime;
    }

    public void setEstimatedCookingTime(int estimatedCookingTime) {
        this.estimatedCookingTime = estimatedCookingTime;
    }

    public int getEstimatedPreparationTime() {
        return estimatedPreparationTime;
    }

    public void setEstimatedPreparationTime(int estimatedPreparationTime) {
        this.estimatedPreparationTime = estimatedPreparationTime;
    }

    public int getPortionSize() {
        return portionSize;
    }

    public void setPortionSize(int portionSize) {
        this.portionSize = portionSize;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * Returns a Product object that represents the sum of all relevant nutritional
     * components in the recipe.
     *
     * @return a Product object with the total nutritional information
     */
    public Product getTotalNutritionalInfo() {
        Product totalNutrition = new Product();
        for (Product product : products) {
            totalNutrition.addNutritionalInfo(product);
        }
        return totalNutrition;
    }
}
