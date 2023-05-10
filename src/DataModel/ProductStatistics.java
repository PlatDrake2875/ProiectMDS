package DataModel;

import DataModel.Product;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

/**
 * The ProductStatistics class provides statistical information
 * about a collection of Product objects.
 */
public class ProductStatistics {

    private final List<Product> products;

    /**
     * Constructs a new ProductStatistics object with the given list of products.
     * @param products the list of products to analyze
     */
    public ProductStatistics(List<Product> products) {
        this.products = products;
    }

    /**
     * Returns the product with the highest calories per 100g.
     * @return the product with the highest calories per 100g
     */
    public Product highestCalorieProduct() {
        return products.stream().max(Comparator.comparing(Product::getKcalPer100g)).orElse(null);
    }



}
