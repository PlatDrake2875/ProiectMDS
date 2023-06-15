package shopScraping.scrape;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * ShopScraper is responsible for connecting to a URL and scraping product data from it.
 * It includes methods to validate a URL and to retrieve various product details like
 * category, name, price, and properties from an Auchan product page.
 */
public class ShopScraper {
    // Constants for the CSS selectors used to extract product information.
    private static final String AUCHAN_PRODUCT_CATEGORY_SELECTOR = ".vtex-breadcrumb-1-x-link.vtex-breadcrumb-1-x-link--productBreadcrumb.vtex-breadcrumb-1-x-link--3.vtex-breadcrumb-1-x-link--productBreadcrumb--3.dib.pv1.link.ph2.c-muted-2.hover-c-link";
    private static final String AUCHAN_PRODUCT_NAME_SELECTOR = ".vtex-store-components-3-x-productBrand--productPage span".trim();
    private static final String AUCHAN_PRODUCT_PRICE_SELECTOR = ".vtex-product-price-1-x-currencyContainer--pdp span";
    private static final String AUCHAN_PRODUCT_PROPERTIES_SELECTOR = ".vtex-product-specifications-1-x-specificationValue.vtex-product-specifications-1-x-specificationValue--first.vtex-product-specifications-1-x-specificationValue--last";
    private static final String TARGET_A_TAG_SELECTOR = "a.vtex-breadcrumb-1-x-link.vtex-breadcrumb-1-x-link--productBreadcrumb.vtex-breadcrumb-1-x-link--1.vtex-breadcrumb-1-x-link--productBreadcrumb--1.dib.pv1.link.ph2.c-muted-2.hover-c-link";
    private static final List<String> targetHrefs = List.of("/brutarie,-cofetarie,-gastro/d", "/bacanie/d", "/lactate,-carne,-mezeluri-&-peste/d", "/fructe-si-legume/d");
    private static final Pattern pattern = Pattern.compile("(\\d+),(\\d+)(\\s*)lei");

    /**
     * Validates the given URL.
     * A URL is considered valid if it is well-formed and starts with http:// or https://.
     *
     * @param url the URL to validate
     * @return true if the URL is valid, false otherwise
     */
    public static boolean isValidURL(String url) {
        try {
            URI uri = new URI(url);
            // URL should start with http:// or https://
            if (uri.getScheme() == null) return false;
            return uri.getScheme().startsWith("http");
        } catch (URISyntaxException e) {
            return false;
        }
    }


    /**
     * Retrieves the category of a product from an Auchan product page.
     *
     * @param doc the HTML document of the product page
     * @return the product's category
     */
    public static Elements getAuchanProductCategory(Document doc) {
        // use the category selector to extract the category from the document
        return doc.select(AUCHAN_PRODUCT_CATEGORY_SELECTOR);
    }

    /**
     * Retrieves the name of a product from an Auchan product page.
     *
     * @param doc the HTML document of the product page
     * @return the product's name
     */
    public static Elements getAuchanProductName(Document doc) {
        // Somehow the select doesn't work if I use constants.
        return doc.select(".vtex-store-components-3-x-productBrand--productPage").select("span");
    }

    /**
     * Retrieves and formats the price of a product from an Auchan product page.
     *
     * @param doc the HTML document of the product page
     * @return the product's price as a string
     */
    public static Elements getAuchanProductPrice(Document doc) {
        // use the price selector to extract the price from the document
        return doc.select(AUCHAN_PRODUCT_PRICE_SELECTOR);
    }

    /**
     * Retrieves and formats the price of a product from an Auchan product page.
     *
     * @param price the price element from the product page
     * @return the product's price as a string
     */
    public static String getAuchanProductPriceToString(Elements price) {
        StringBuilder priceBuilder = new StringBuilder();

        for (Element element : price) {
            String text = element.text();
            if (text.matches("[0-9,]+")) {
                priceBuilder.append(text.replace(",", "."));
            } else {
                break;
            }
        }

        return priceBuilder.toString();
    }



    /**
     * Retrieves the properties of a product from an Auchan product page.
     *
     * @param doc the HTML document of the product page
     * @return the product's properties
     */
    public static Elements getAuchanProductProperties(Document doc) {
        // use the properties selector to extract the product properties from the document
        return doc.select(AUCHAN_PRODUCT_PROPERTIES_SELECTOR);
    }

    /**
     * Checks if the given href contains any of the target hrefs.
     *
     * @param href the href to check
     * @return true if the href contains any of the target hrefs, false otherwise
     */
    public static boolean containsTargetHref(String href) {
        return targetHrefs.stream().anyMatch(href::contains);
    }

    // New method to check if the 'a' elements contain any target href
    public static boolean checkATagsForHref(Document doc) {
        return classContainsHref(doc);
    }


    static boolean classContainsHref(Document doc) {
        Elements elements = doc.select(ShopScraper.TARGET_A_TAG_SELECTOR);

        for (Element element : elements) {
            if (containsTargetHref(element.attr("href"))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Connects to the provided URL and retrieves the HTML document.
     *
     * @param urlProduct the URL to connect to
     * @return the HTML document retrieved from the URL
     * @throws IOException if an error occurs during connection
     */
    public static Document connectToURL(String urlProduct) throws IOException {
        if (urlProduct == null || urlProduct.isEmpty()) {
            throw new IllegalArgumentException("urlProduct is null or empty");
        }
        // connect to the url and retrieve the document
        return Jsoup.connect(urlProduct).get();
    }

}
