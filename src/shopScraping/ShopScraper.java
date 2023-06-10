package shopScraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ShopScraper is responsible for connecting to a URL and scraping product data from it.
 */
public class ShopScraper {
    private static final Set<String> visitedURLs = new HashSet<>();
    private static final Logger LOGGER = Logger.getLogger(ShopScraper.class.getName());

    /**
     * Checks if the given url is a valid URL.
     *
     * @param url the url to validate
     * @return true if the url is valid, false otherwise
     */
    static boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }

    /**
     * Connects to the provided URL and retrieves the HTML document.
     *
     * @param urlProduct the URL to connect to
     * @return the HTML document retrieved from the URL
     * @throws IOException if an error occurs during connection
     */
    Document connectToURL(String urlProduct) throws IOException {
        return Jsoup.connect(urlProduct).get();
    }

    /**
     * Retrieves the category of a product from an Auchan product page.
     *
     * @param doc the HTML document of the product page
     * @return the product's category
     */
    static Elements getAuchanProductCategory(Document doc) {
        return doc.select(".vtex-breadcrumb-1-x-link.vtex-breadcrumb-1-x-link--productBreadcrumb.vtex-breadcrumb-1-x-link--3.vtex-breadcrumb-1-x-link--productBreadcrumb--3.dib.pv1.link.ph2.c-muted-2.hover-c-link");
    }

    /**
     * Retrieves the name of a product from an Auchan product page.
     *
     * @param doc the HTML document of the product page
     * @return the product's name
     */
    static Elements getAuchanProductName(Document doc) {
        return doc.select(".vtex-store-components-3-x-productBrand--productPage").select("span");
    }

    /**
     * Retrieves the price of a product from an Auchan product page.
     *
     * @param doc the HTML document of the product page
     * @return the product's price
     */
    static Elements getAuchanProductPrice(Document doc) {
        return doc.select(".vtex-product-price-1-x-currencyContainer--pdp").select("span");
    }

    /**
     * Converts the price of a product from Elements to a String.
     *
     * @param price the price to convert
     * @return the product's price as a string
     */
    static String getAuchanProductPriceToString(Elements price) {
        return price.get(1).text() + "." + price.get(3).text();
    }

    /**
     * Retrieves the properties of a product from an Auchan product page.
     *
     * @param doc the HTML document of the product page
     * @return the product's properties
     */
    static Elements getAuchanProductProperties(Document doc) {
        return doc.select(".vtex-product-specifications-1-x-specificationValue.vtex-product-specifications-1-x-specificationValue--first.vtex-product-specifications-1-x-specificationValue--last");
    }
}
