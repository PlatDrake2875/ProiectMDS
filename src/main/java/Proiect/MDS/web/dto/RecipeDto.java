package Proiect.MDS.web.dto;

import Proiect.MDS.web.models.Recipe;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RecipeDto {
    private int id;
    private String photoURL;
    private String recipeName;
    private int estimatedCookingTime;
    private int estimatedPreparationTime;
    private int portionSize;
    private String products;

}
