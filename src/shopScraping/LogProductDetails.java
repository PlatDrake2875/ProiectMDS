package shopScraping;

import org.jsoup.select.Elements;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * LogProductDetails class is responsible for logging the details of a product.
 */
public class LogProductDetails {
    private static final Logger LOGGER = Logger.getLogger(LogProductDetails.class.getName());

    /**
     * Logs the product details which include category, name, price, and properties of the product.
     *
     * @param category    The category of the product.
     * @param name        The name of the product.
     * @param priceString The price of the product as a String.
     * @param properties  The properties of the product.
     */
    public static void logDetails(Elements category, Elements name, String priceString, Elements properties) {
        LOGGER.log(Level.INFO, "category: {0}", category.text());
        LOGGER.log(Level.INFO, "price: {0}", priceString);
        LOGGER.log(Level.INFO, "mname: {0}", name.get(0).text());
        LOGGER.log(Level.INFO, "properties:");

        properties.forEach(elem -> LOGGER.log(Level.INFO, "name: {0},value: {1}", new String[]{
                elem.attr("data-specification-name"),
                elem.attr("data-specification-value")
        }));
    }

    /**
     * Logs the insertion of a product into the database.
     *
     * @param productName The name of the product being inserted.
     */
    public static void logProductInsertion(String productName) {
        LOGGER.log(Level.INFO, "Product inserted into database: {0}", productName);
    }

    /**
     * Logs the update of a product in the database.
     *
     * @param productName The name of the product being updated.
     */
    public static void logProductUpdate(String productName) {
        LOGGER.log(Level.INFO, "Product updated in database: {0}", productName);
    }

    /**
     * Logs the product that was not updated due to not being eligible for update.
     *
     * @param productName The name of the product not updated.
     */
    public static void logProductNotEligibleForUpdate(String productName) {
        LOGGER.log(Level.INFO, "Product not eligible for update: {0}", productName);
    }
}
