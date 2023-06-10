package shopScraping;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ProductScraper is responsible for scraping product details from a given URL.
 */
public class ProductScraper {
    private static final Logger LOGGER = Logger.getLogger(ProductScraper.class.getName());

    /**
     * Connects to the provided URL using the given ShopScraper and retrieves product details such as category, name,
     * price, and other properties. These details are then stored in a Map and returned.
     *
     * @param scraper the ShopScraper used to connect to the URL and retrieve the HTML document
     * @param urlProduct the URL of the product to scrape details from
     * @return a Map containing the product's details, where each key is the detail's name and its value is the detail
     *
     * @throws IOException if an error occurs during connection
     */
    public Map<String, String> getProductDetails(ShopScraper scraper, String urlProduct) throws IOException {
        Map<String, String> productProperties = new HashMap<>();

        try {
            Document doc = scraper.connectToURL(urlProduct);
            Elements category = ShopScraper.getAuchanProductCategory(doc);
            Elements name = ShopScraper.getAuchanProductName(doc);
            Elements price = ShopScraper.getAuchanProductPrice(doc);
            String priceString = ShopScraper.getAuchanProductPriceToString(price);
            Elements properties = ShopScraper.getAuchanProductProperties(doc);

            productProperties.put("Category", category.text());
            productProperties.put("Name", name.get(0).text());
            productProperties.put("Price", priceString);
            for (Element property : properties) {
                productProperties.put(property.attr("data-specification-name"), property.attr("data-specification-value"));
            }

            LogProductDetails.logDetails(category, name, priceString, properties);

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }

        return productProperties;
    }
}
