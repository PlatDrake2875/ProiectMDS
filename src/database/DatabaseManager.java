package database;

import DataModel.Product;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseManager {
    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());

    private final Database database;
    private final int UPDATE_INTERVAL_HOURS = 24;

    public DatabaseManager(Database database) {
        this.database = database;
    }

    public void insertOrUpdateProduct(Product product) {
        try {
            Product existingProduct = database.getProductByName(product.getName());
            if (existingProduct != null && isUpdateTime()) {
                database.updateProduct(
                        existingProduct.getId(),
                        product.getName(),
                        product.getCategory(),
                        BigDecimal.valueOf(product.getPrice()),
                        product.getProductType(),
                        product.getStorageConditions(),
                        BigDecimal.valueOf(product.getWeight()),
                        product.getShelfLife(),
                        product.getIngredients(),
                        BigDecimal.valueOf(product.getKcalPer100g()),
                        BigDecimal.valueOf(product.getKjPer100g()),
                        BigDecimal.valueOf(product.getFats()),
                        BigDecimal.valueOf(product.getSaturatedFats()),
                        BigDecimal.valueOf(product.getCarbohydrates()),
                        BigDecimal.valueOf(product.getSugars()),
                        BigDecimal.valueOf(product.getSalt()),
                        BigDecimal.valueOf(product.getFiber()),
                        BigDecimal.valueOf(product.getProteins())
                );
                LOGGER.log(Level.INFO, "Product updated successfully");
                updateLastModificationTime();
            } else {
                database.insertProduct(
                        product.getName(),
                        product.getCategory(),
                        BigDecimal.valueOf(product.getPrice()),
                        product.getProductType(),
                        product.getStorageConditions(),
                        BigDecimal.valueOf(product.getWeight()),
                        product.getShelfLife(),
                        product.getIngredients(),
                        BigDecimal.valueOf(product.getKcalPer100g()),
                        BigDecimal.valueOf(product.getKjPer100g()),
                        BigDecimal.valueOf(product.getFats()),
                        BigDecimal.valueOf(product.getSaturatedFats()),
                        BigDecimal.valueOf(product.getCarbohydrates()),
                        BigDecimal.valueOf(product.getSugars()),
                        BigDecimal.valueOf(product.getSalt()),
                        BigDecimal.valueOf(product.getFiber()),
                        BigDecimal.valueOf(product.getProteins())
                );
                LOGGER.log(Level.INFO, "Product inserted successfully");
                updateLastModificationTime();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inserting or updating product", e);
        }
    }

    public void insertOrUpdateProductStatistics(Product product) {
        try {
            Product existingProduct = database.getProductStatisticsByName(product.getName());
            if (existingProduct != null) {
                database.updateProductStatistics(
                        existingProduct.getId(),
                        product.getAverageRating(),
                        product.getNumberOfReviews()
                );
                LOGGER.log(Level.INFO, "Product statistics updated successfully");
            } else {
                database.insertProductStatistics(
                        product.getName(),
                        product.getAverageRating(),
                        product.getNumberOfReviews()
                );
                LOGGER.log(Level.INFO, "Product statistics inserted successfully");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inserting or updating product statistics", e);
        }
    }

}
