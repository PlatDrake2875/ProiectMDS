package shopScraping;

import DataModel.Product;
import org.jsoup.Jsoup;

import javax.lang.model.util.Elements;
import javax.swing.text.Document;
import java.util.logging.Logger;

public class WebScraper {

    private static final Logger LOGGER = Logger.getLogger(WebScraper.class.getName());

    public Product getProductAuchan(String urlProduct) throws IOException {
        Document doc = Jsoup.connect(urlProduct).get();
        Elements category = getAuchanProductCategory(doc);
        Elements name = getAuchanProductName(doc);
        Elements price = getAuchanProductPrice(doc);
        String priceString = getAuchanProductPriceToString(price);
        Elements properties = getAuchanProductProperties(doc);

        logProductDetails(category, name, priceString, properties);

        Product product = new Product(); // Instantiate a new Product object
        // Set the properties of the product object using the scraped data
        // ...

        return product;
    }

    // The remaining helper methods...
}
