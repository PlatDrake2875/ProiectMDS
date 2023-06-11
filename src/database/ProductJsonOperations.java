package database;

import DataModel.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class handles operations related to the product JSON file.
 */
public class ProductJsonOperations {
    private static final Logger LOGGER = Logger.getLogger(ProductJsonOperations.class.getName());
    private final ProductTableOperations pto;

    /**
     * Constructor for the class. Requires a ProductTableOperations object.
     *
     * @param productTableOperations a ProductTableOperations object
     */
    public ProductJsonOperations(ProductTableOperations productTableOperations) {
        this.pto = productTableOperations;
    }

    /**
     * Reads product information from a JSON file and imports it into the database.
     *
     * @param filename The name of the JSON file
     */
    public void importProductsFromJson(String filename) {
        List<Product> products = readProductsFromJson(filename);
        if (products == null) return;

        for (Product product : products) {
            handleProductImport(product);
        }
    }

    /**
     * Reads product information from a JSON file.
     *
     * @param filename The name of the JSON file
     * @return A list of Product objects, or null if an error occurred
     */
    private List<Product> readProductsFromJson(String filename) {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(filename);

        try {
            return mapper.readValue(file, new TypeReference<List<Product>>() {
            });
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading products from JSON file:", e);
            return null;
        }
    }

    /**
     * Handles the import of a single product.
     *
     * @param product The product to be imported
     */
    private void handleProductImport(Product product) {
        Product existingProduct = pto.getProductByName(product.getName());

        if (existingProduct == null) {
            pto.insertProduct(product);
        } else {
            product.setId(existingProduct.getId());
            pto.updateProduct(product);
        }
    }

    /**
     * Exports product information from the database to a JSON file.
     *
     * @param filename The name of the JSON file
     */
    public void exportProductsToJson(String filename) {
        List<Product> products = pto.getAllProducts();
        if (products.isEmpty()) {
            LOGGER.log(Level.INFO, "No products to export");
            return;
        }

        writeProductsToJson(filename, products);
    }

    /**
     * Writes product information to a JSON file.
     *
     * @param filename The name of the JSON file
     * @param products The products to be exported
     */
    private void writeProductsToJson(String filename, List<Product> products) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            mapper.writeValue(Paths.get(filename).toFile(), products);
            LOGGER.log(Level.INFO, "Products exported successfully to {0}", filename);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error exporting products to JSON:", e);
        }
    }
}
