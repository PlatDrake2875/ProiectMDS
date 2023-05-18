import DataModel.Ingredient;
import DataModel.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipesList {
    public void createRecipes(boolean print) {

        List<Recipe> recipes = new ArrayList<>();

        List<Ingredient> sarmale = new ArrayList<>();
        sarmale.add(new Ingredient("ground beef", 1000D, "grams"));
        sarmale.add(new Ingredient("rice", 200D, "grams"));
        sarmale.add(new Ingredient("vegetable oil", 150D, "milliliter"));
        sarmale.add(new Ingredient("onion", 4D, "piece"));
        sarmale.add(new Ingredient("tomato paste", 16D, "grams"));
        sarmale.add(new Ingredient("tomato juice", 1000D, "milliliter"));
        sarmale.add(new Ingredient("sour cabbage", 2D, "piece"));
        sarmale.add(new Ingredient("black pepper", 1D, "piece"));
        sarmale.add(new Ingredient("bay leaves", 1D, "piece"));
        sarmale.add(new Ingredient("thyme", 1D, "piece"));
        sarmale.add(new Ingredient("ground black pepper", 1D, "piece"));
        sarmale.add(new Ingredient("paprika", 1D, "piece"));
        sarmale.add(new Ingredient("salt", 1D, "piece"));

        recipes.add(new Recipe("Sarmale", "", 50, 150, 50, null, sarmale));

        List<Ingredient> snowWhite = new ArrayList<>();

        snowWhite.add(new Ingredient("flour", 600D, "grams"));
        snowWhite.add(new Ingredient("butter", 500D, "grams"));
        snowWhite.add(new Ingredient("egg", 2D, "piece"));
        snowWhite.add(new Ingredient("sugar", 400D, "grams"));
        snowWhite.add(new Ingredient("vanilla sugar", 8D, "grams"));
        snowWhite.add(new Ingredient("sour cream", 50D, "grams"));
        snowWhite.add(new Ingredient("baker's ammonia", 4D, "grams"));
        snowWhite.add(new Ingredient("lemon zest", 3D, "piece"));
        snowWhite.add(new Ingredient("milk", 1000D, "milliliter"));
        snowWhite.add(new Ingredient("corn starch", 130D, "grams"));
        snowWhite.add(new Ingredient("vanilla bean", 1D, "piece"));

        recipes.add(new Recipe("Romanian lemon cake", "", 16, 120, 20, null, snowWhite));

        List<Ingredient> cheesecake = new ArrayList<>();
        cheesecake.add(new Ingredient("digestive biscuits", 350D, "grams"));
        cheesecake.add(new Ingredient("unsalted butter", 100D, "grams"));
        cheesecake.add(new Ingredient("cream cheese", 300D, "grams"));
        cheesecake.add(new Ingredient("caster sugar", 16D, "grams"));
        cheesecake.add(new Ingredient("vanilla extract", 15D, "milliliter"));
        cheesecake.add(new Ingredient("double cream", 300D, "milliliter"));
        cheesecake.add(new Ingredient("dark chocolate 70%", 200D, "grams"));
        cheesecake.add(new Ingredient("white chocolate", 100D, "grams"));

        recipes.add(new Recipe("Chocolate cheesecake", "", 16, 60, 10, null, cheesecake));

        List<Ingredient> tomatoSoup = new ArrayList<>();
        tomatoSoup.add(new Ingredient("olive oil", 60D, "milliliter"));
        tomatoSoup.add(new Ingredient("bay leaves", 1D, "piece"));
        tomatoSoup.add(new Ingredient("chopped onion", 43D, "grams"));
        tomatoSoup.add(new Ingredient("chopped garlic", 16D, "grams"));
        tomatoSoup.add(new Ingredient("tomato", 500D, "grams"));
        tomatoSoup.add(new Ingredient("water", 250D, "milliliter"));
        tomatoSoup.add(new Ingredient("salt", 1D, "piece"));
        tomatoSoup.add(new Ingredient("sugar", 8D, "grams"));
        tomatoSoup.add(new Ingredient("ground black pepper", 1D, "piece"));
        tomatoSoup.add(new Ingredient("chopped parsley", 16D, "grams"));
        tomatoSoup.add(new Ingredient("sour cream", 60D, "grams"));

        recipes.add(new Recipe("Tomato soup", "", 3, 30, 4, null, tomatoSoup));

        List<Ingredient> icecream = new ArrayList<>();
        icecream.add(new Ingredient("egg yolk", 8D, "piece"));
        icecream.add(new Ingredient("sugar", 128D, "grams"));
        icecream.add(new Ingredient("sour cream", 128D, "grams"));
        icecream.add(new Ingredient("salt", 2D, "grams"));
        icecream.add(new Ingredient("vanilla extract", 30D, "milliliter"));

        recipes.add(new Recipe("Vanilla ice cream", "", 2, 45, 2, null, icecream));

        if (print == true){
            for(int i = 0; i < recipes.size(); i++){
                System.out.println(recipes.get(i).getRecipeName());
                List<Ingredient> list = recipes.get(i).getIngredients();
                for(int j = 0; j < list.size(); j++){
                    Ingredient ingredient = list.get(j);
                    System.out.println(ingredient.getName());
                }
                System.out.println("\n\n");
            }
        }



    }
}