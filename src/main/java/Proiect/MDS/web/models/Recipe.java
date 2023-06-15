package Proiect.MDS.web.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "recipes")

public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //makes sure each recipe has a unique id
    private int id;
    private String photoURL;
    private String recipeName;
    private int estimatedCookingTime;
    private int estimatedPreparationTime;
    private int portionSize;
    private String products; //contains id's of products

    public Recipe(String recipeName, String photoURL, int estimatedCookingTime, int estimatedPreparationTime, int portionSize, String products) {
        this.recipeName = recipeName;
        this.photoURL = photoURL;
        this.estimatedCookingTime = estimatedCookingTime;
        this.estimatedPreparationTime = estimatedPreparationTime;
        this.portionSize = portionSize;
        this.products = products;
    }

    // Getters and setters
    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
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

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPortionSize() {
        return portionSize;
    }

    public void setPortionSize(int portionSize) {
        this.portionSize = portionSize;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    /**
     * Returns a Product object that represents the sum of all relevant nutritional
     * components in the recipe.
     *
     * @return a Product object with the total nutritional information
     */
//    public Product getTotalNutritionalInfo() {
//        Product totalNutrition = new Product();
//        for (Product product : products) {
//            totalNutrition.addNutritionalInfo(product);
//        }
//        return totalNutrition;
//    }
}
