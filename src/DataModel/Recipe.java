package DataModel;

import java.util.List;
import java.util.UUID;

public class Recipe {
    private UUID id;
    private String name;
    private String instructions;
    private Integer servings;
    private Integer time; // cooking time in minutes
    private List<Ingredient> ingredients;

    public Recipe (String name, String instructions, Integer servings, Integer time, List<Ingredient> ingredients) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.instructions = instructions;
        this.servings = servings;
        this.time = time;
        this.ingredients = ingredients;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
