package shopScraping;

import DataModel.Product;
import database.Database;
import org.javatuples.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.logging.*;

/**
 * Class responsible for crawling through webpages to retrieve product data.
 */
public class ProductCrawler {
    private static final Set<String> visitedURLs = new HashSet<>();
    private static final Logger LOGGER = new AppLogger(ProductCrawler.class).getLogger();

    private static final String[] crawlingURLs = new String[]{"https://www.auchan.ro/brutarie-cofetarie-gastro/c", "https://www.auchan.ro/bauturi-si-tutun/c", "https://www.auchan.ro/bacanie/c", "https://www.auchan.ro/lactate-carne-mezeluri---peste/c", "https://www.auchan.ro/fructe-si-legume/c"};

    private final ShopScraper shopScraper;
    private final ProductScraper productScraper;
    private final Database db;

    public ProductCrawler(ShopScraper shopScraper, ProductScraper productScraper, Database db) {
        this.shopScraper = shopScraper;
        this.productScraper = productScraper;
        this.db = db;
    }

    /**
     * Method to start the product crawling process.
     *
     * @throws IOException if an I/O error occurs during file writing
     */
    public void getProductsAuchan() throws IOException {
        try (FileWriter crawledURLs = new FileWriter("CrawledURLS.txt")) {
            Queue<Pair<Integer, String>> queue = initializeURLQueue();
            startCrawling(queue);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while crawling URLs: {0}", e.getMessage());
        }
    }

    /**
     * Initializes the URL queue with the start URLs.
     *
     * @return a Queue containing the start URLs
     */
    private Queue<Pair<Integer, String>> initializeURLQueue() {
        Queue<Pair<Integer, String>> queue = new LinkedList<>();
        for (String url : crawlingURLs) {
            queue.add(new Pair<>(1, url));
            visitedURLs.add(url);
        }
        return queue;
    }

    /**
     * Main process to start the crawling operation.
     * Crawls only the first 5 depth levels.
     *
     * @param queue URL queue
     */
    private void startCrawling(Queue<Pair<Integer, String>> queue) {
        while (!queue.isEmpty()) {
            Pair<Integer, String> urlPair = queue.poll();
            if (urlPair.getValue0() < 5) {
                processURL(queue, urlPair);
            }
        }
    }

    /**
     * Connects to the URL, gets the document and processes all the links.
     *
     * @param queue   URL queue
     * @param urlPair the URL and depth level
     */
    private void processURL(Queue<Pair<Integer, String>> queue, Pair<Integer, String> urlPair) {
        try {
            String url = urlPair.getValue1();
            Document doc = shopScraper.connectToURL(url);
            Elements links = doc.select("a[href]");
            processLinks(queue, urlPair.getValue0(), links);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while connecting to URL: {0}", e.getMessage());
        }
    }

    /**
     * Processes a collection of links, determining if each link is a product page
     * and either updating the existing product or inserting a new one to the database.
     *
     * @param queue      The queue of URLs yet to be processed.
     * @param depthLevel The depth level of the crawl.
     * @param links      The collection of links to process.
     * @throws IOException if there is an issue with fetching the product details.
     */
    private void processLinks(Queue<Pair<Integer, String>> queue, int depthLevel, Elements links) throws IOException {
        for (Element link : links) {
            String absHref = link.attr("abs:href");

            if (shouldProcessLink(absHref)) {
                logLink(depthLevel, absHref);

                if (isProductPage(absHref)) {
                    processProduct(absHref);
                }

                visitedURLs.add(absHref);
                queue.add(new Pair<>(depthLevel + 1, absHref));
            }
        }
    }

    /**
     * Determines if a URL should be processed based on its validity and visited status.
     *
     * @param absHref The absolute URL to be checked.
     * @return true if the URL is valid and has not been visited; false otherwise.
     */
    private boolean shouldProcessLink(String absHref) {
        return ShopScraper.isValidURL(absHref) && !visitedURLs.contains(absHref);
    }

    /**
     * Logs the depth level and URL of the link being processed.
     *
     * @param depthLevel The depth level of the crawl.
     * @param absHref    The absolute URL being processed.
     */
    private void logLink(int depthLevel, String absHref) {
        LOGGER.log(Level.INFO, "{0} {1} ", new String[]{String.valueOf(depthLevel), absHref});
    }


    /**
     * Determines if a URL is a product page.
     *
     * @param absHref The absolute URL to be checked.
     * @return true if the URL is a product page; false otherwise.
     */
    private boolean isProductPage(String absHref) {
        return absHref.endsWith("/p") || absHref.endsWith("/p#");
    }

    /**
     * Processes the product page, either updating the existing product or inserting a new one.
     *
     * @param absHref The absolute URL of the product page.
     */
    private void processProduct(String absHref) throws IOException {
        Product product = productScraper.getProductDetails(shopScraper, absHref);
        Product existingProduct = db.getProductByName(product.getName());

        if (existingProduct != null) {
            processExistingProduct(product, existingProduct);
        } else {
            insertNewProduct(product);
        }
    }

    /**
     * Handles the product if it exists in the database.
     *
     * @param product         The product fetched from the website.
     * @param existingProduct The product fetched from the database.
     */
    private void processExistingProduct(Product product, Product existingProduct) {
        LocalDateTime lastModified = existingProduct.getLastModified();
        LocalDateTime now = LocalDateTime.now(ZoneId.of("ECT"));
        long hoursElapsed = Duration.between(lastModified, now).toHours();

        if (hoursElapsed >= 12) {
            updateProduct(product);
        } else {
            LogProductDetails.logProductNotEligibleForUpdate(product.getName());
        }
    }

    /**
     * Inserts a new product to the database.
     *
     * @param product The product to be inserted.
     */
    private void insertNewProduct(Product product) {
        db.insertProduct(product);
        LogProductDetails.logProductInsertion(product.getName());
    }

    /**
     * Updates the existing product in the database.
     *
     * @param product The product to be updated.
     */
    private void updateProduct(Product product) {
        db.updateProduct(product);
        LogProductDetails.logProductUpdate(product.getName());
    }
}
