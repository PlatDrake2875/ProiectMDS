package Proiect.MDS.web.repository;

import Proiect.MDS.web.models.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    /**
     * Returns a Product instance by its ID.
     *
     * @param id The ID of the Product to retrieve.
     * @return An Optional of the found Product instance.
     */
    Optional<Product> findById(int id);

    /**
     * Returns all Product instances.
     *
     * @return A List of all Product instances.
     */
    List<Product> findAll();

    // Existing method
    Product getProductById(int id);

}
