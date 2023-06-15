package Proiect.MDS.web.repository.impl;

import Proiect.MDS.web.database.Database;
import Proiect.MDS.web.database.ProductTableOperations;
import Proiect.MDS.web.models.Product;
import Proiect.MDS.web.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * This class implements the ProductRepository interface and provides
 * functionality for interacting with the ProductTable in the database.
 */
@Service
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductTableOperations productTableOperations;

    /**
     * The constructor initializes the ProductTableOperations instance with a specific database configuration.
     */
    public ProductRepositoryImpl() {
        this.productTableOperations = new ProductTableOperations(new Database("jdbc:mysql://localhost/dbProducts", "root", "2875"));
    }

    /**
     * Retrieves a product by its ID from the database.
     *
     * @param id The ID of the product.
     * @return The Product with the given ID, or null if no such Product exists.
     */
    @Override
    public Product getProductById(int id) {
        return productTableOperations.getProductByColumnValue("id", Integer.toString(id));
    }

    /**
     * Retrieves a product by its ID from the database wrapped in an Optional.
     *
     * @param id The ID of the product.
     * @return An Optional that contains the Product if it exists, or is empty if no such Product exists.
     */
    @Override
    public Optional<Product> findById(int id) {
        Product product = getProductById(id);
        return Optional.ofNullable(product);
    }

    /**
     * Retrieves all products from the database.
     *
     * @return A list of all Products in the database.
     */
    @Override
    public List<Product> findAll() {
        return productTableOperations.getAllProducts();
    }
}
