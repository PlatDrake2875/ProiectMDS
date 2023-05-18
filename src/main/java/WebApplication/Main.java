package WebApplication;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        RecipesList recipesList = new RecipesList();
        recipesList.createRecipes(false);
        SpringApplication.run(Main.class, args);
    }
}
