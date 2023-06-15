package Proiect.MDS.web.dto;

import Proiect.MDS.web.models.Recipe;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Data Transfer Object for transferring Recipe data between different parts of the application.
 */
@Data
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDto {
    /**
     * The unique ID of the Recipe.
     */
    private int id;

    /**
     * URL of the Recipe photo.
     */
    private String photoURL;

    /**
     * The name of the Recipe.
     */
    private String recipeName;

    /**
     * Estimated cooking time for the Recipe in minutes.
     */
    private int estimatedCookingTime;

    /**
     * Estimated preparation time for the Recipe in minutes.
     */
    private int estimatedPreparationTime;

    /**
     * The number of portions that the Recipe produces.
     */
    private int portionSize;

    /**
     * The products (ingredients) needed for the Recipe.
     */
    private String products;
}