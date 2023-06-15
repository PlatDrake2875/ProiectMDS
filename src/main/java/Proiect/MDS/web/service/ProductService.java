package Proiect.MDS.web.service;

import Proiect.MDS.web.dto.ProductDto;
import Proiect.MDS.web.dto.RecipeDto;
import Proiect.MDS.web.models.Product;
import Proiect.MDS.web.models.Recipe;

import java.util.List;
import java.util.Optional;

/**
 * Interface for a service that manages Product instances.
 */
public interface ProductService {
    /**
     * Retrieves a Product instance by its ID.
     *
     * @param id The ID of the Product to retrieve.
     * @return An Optional containing the found Product instance, or empty if no Product was found.
     */
    Optional<Product> getProductById(int id);

    /**
     * Retrieves all Product instances.
     *
     * @return A list of all ProductDto instances.
     */
    List<ProductDto> getAllProducts();
}
