package shopScraping;

import DataModel.Product;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ProductScraper is responsible for scraping product details from a given URL. It employs JSoup library
 * for HTML parsing and extraction of elements.
 *
 * It makes use of {@link ShopScraper} to connect to the URL and retrieve the HTML document. It then
 * parses the document to extract the product details and uses the {@link Product.Builder} to construct
 * a Product instance which encapsulates the product details.
 *
 * @author Dragonul
 * @version 1.0
 * @since 2023-06-10
 */
public class ProductScraper {
    private static final Logger LOGGER = Logger.getLogger(ProductScraper.class.getName());
    private static final Map<String, BiConsumer<Product.Builder, String>> propertySetters = new HashMap<>();

    // This static block initializes the propertySetters map with corresponding BiConsumer instances.
    static {
        propertySetters.put("Tip Produs", Product.Builder::productType);
        propertySetters.put("Specialitate", Product.Builder::speciality);
        propertySetters.put("Conditii de pastrare", Product.Builder::storageConditions);
        propertySetters.put("Greutate", (builder, value) -> builder.weight(new BigDecimal(value)));
        propertySetters.put("Termen de valabilitate", Product.Builder::shelfLife);
        propertySetters.put("Ingrediente", Product.Builder::ingredients);
        propertySetters.put("Kcal pe 100g sau 100ml", (builder, value) -> builder.kcalPer100g(new BigDecimal(value)));
        propertySetters.put("KJ pe 100g sau 100ml", (builder, value) -> builder.kjPer100g(new BigDecimal(value)));
        propertySetters.put("Grasimi (g sau ml)", (builder, value) -> builder.fats(new BigDecimal(value)));
        propertySetters.put("Acizi grasi saturati (g sau ml)", (builder, value) -> builder.saturatedFats(new BigDecimal(value)));
        propertySetters.put("Glucide (g sau ml)", (builder, value) -> builder.carbohydrates(new BigDecimal(value)));
        propertySetters.put("Zaharuri (g sau ml)", (builder, value) -> builder.sugars(new BigDecimal(value)));
        propertySetters.put("Sare (g sau ml)", (builder, value) -> builder.salt(new BigDecimal(value)));
        propertySetters.put("Proteine (g sau ml)", (builder, value) -> builder.proteins(new BigDecimal(value)));
    }

    /**
     * Connects to the provided URL using the given ShopScraper and retrieves product details such as category, name,
     * price, and other properties. These details are then stored in a Product object and returned.
     *
     * @param scraper the ShopScraper used to connect to the URL and retrieve the HTML document
     * @param urlProduct the URL of the product to scrape details from
     * @return a Product containing the product's details
     *
     * @throws IOException if an error occurs during connection
     */
    public Product getProductDetails(ShopScraper scraper, String urlProduct) throws IOException {
        Product product = new Product.Builder().build();
        try {
            Document doc = scraper.connectToURL(urlProduct);
            Elements category = ShopScraper.getAuchanProductCategory(doc);
            Elements name = ShopScraper.getAuchanProductName(doc);
            Elements price = ShopScraper.getAuchanProductPrice(doc);
            String priceString = ShopScraper.getAuchanProductPriceToString(price);
            Elements properties = ShopScraper.getAuchanProductProperties(doc);

            Product.Builder builder = new Product.Builder()
                    .name(name.get(0).text())
                    .category(category.text())
                    .price(new BigDecimal(priceString));

            for (Element property : properties) {
                String propertyName = property.attr("data-specification-name");
                String propertyValue = property.attr("data-specification-value");

                if (propertySetters.containsKey(propertyName)) {
                    propertySetters.get(propertyName).accept(builder, propertyValue);
                }
            }

            product = builder.build();

            LogProductDetails.logDetails(category, name, priceString, properties);

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }

        return product;
    }
}