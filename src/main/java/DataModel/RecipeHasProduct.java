package DataModel;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name="recipes_have_products")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecipeHasProduct {

    @Id
    private UUID idRecipe;

    @Id
    private UUID idProduct;
}
