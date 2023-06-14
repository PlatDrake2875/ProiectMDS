package Proiect.MDS.web.shopScraping;

import Proiect.MDS.web.models.Product;
import Proiect.MDS.web.database.ProductTableOperations;
import org.javatuples.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class responsible for crawling through webpages to retrieve product data.
 */
public class ProductCrawler {
    private static final Set<String> visitedURLs = new HashSet<>();
    private static final Logger LOGGER = new AppLogger(ProductCrawler.class).getLogger();
    private static final String[] crawlingURLs = new String[]{"https://www.auchan.ro/brutarie-cofetarie-gastro/c", "https://www.auchan.ro/bacanie/c", "https://www.auchan.ro/lactate-carne-mezeluri---peste/c", "https://www.auchan.ro/fructe-si-legume/c"};

    private final ShopScraper shopScraper;
    private final ProductScraper productScraper;
    private final ProductTableOperations pto;


    public ProductCrawler(ShopScraper shopScraper, ProductScraper productScraper, ProductTableOperations pto) {
        this.shopScraper = shopScraper;
        this.productScraper = productScraper;
        this.pto = pto;
    }

    /**
     * Method to start the product crawling process.
     */
    public void getProductsAuchan() {
        Queue<Pair<Integer, String>> queue = initializeURLQueue();
        startCrawling(queue);
    }


    /**
     * Initializes the URL queue with the start URLs.
     *
     * @return a Queue containing the start URLs
     */
    private Queue<Pair<Integer, String>> initializeURLQueue() {
        Queue<Pair<Integer, String>> queue = new LinkedList<>();
        Arrays.stream(crawlingURLs).forEach(url -> {
            queue.add(new Pair<>(1, url));
            visitedURLs.add(url);
        });
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
            visitedURLs.add(urlPair.getValue1());
            processURL(queue, urlPair);
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
            System.out.println("Processing URL: " + urlPair.getValue1());
            processValidURL(queue, urlPair);
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    /**
     * Handles an IOException by logging the error.
     *
     * @param e the IOException that occurred
     */
    private void handleIOException(IOException e) {
        // Log the error
        LOGGER.log(Level.SEVERE, "An error occurred while trying to write to the file: {0}", e.getMessage());
    }

    /**
     * Connects to a valid URL, gets the document, and processes the links.
     *
     * @param queue   URL queue
     * @param urlPair the URL and depth level
     * @throws IOException if an I/O error occurs
     */
    private void processValidURL(Queue<Pair<Integer, String>> queue, Pair<Integer, String> urlPair) throws IOException {
        String url = urlPair.getValue1();
        if (ShopScraper.isValidURL(url)) {
            System.out.println("Processing valid URL: " + urlPair.getValue1());
            Document doc = ShopScraper.connectToURL(url);
            Elements links = doc.select("a[href]");
            processLinks(queue, urlPair.getValue0(), links);
        }
    }


    /**
     * Processes a collection of links concurrently, determining if each link is a product page
     * and either updating the existing product or inserting a new one to the database.
     *
     * @param queue      The queue of URLs yet to be processed.
     * @param depthLevel The depth level of the crawl.
     * @param links      The collection of links to process.
     */
    private void processLinks(Queue<Pair<Integer, String>> queue, int depthLevel, Elements links) throws IOException {
        for (String absHref : links.stream().map(link -> link.attr("abs:href")).toList()) {
            if (shouldProcessLink(absHref)) {
                processLink(queue, depthLevel, absHref);
            }
        }
        System.out.println("Queue size: " + queue.size());
        queue.forEach(pair -> System.out.println("Key: " + pair.getValue0() + ", Value: " + pair.getValue1()));
    }


    /**
     * Processes a link by logging it, checking if it leads to a product page, and adding it to the visited URLs.
     *
     * @param queue      The queue to which the link will be added.
     * @param depthLevel The depth level of the link in the crawl hierarchy.
     * @param absHref    The absolute URL of the link.
     * @throws IOException If an I/O error occurs.
     */
    private void processLink(Queue<Pair<Integer, String>> queue, int depthLevel, String absHref) throws IOException {
        System.out.println("Processing link: " + absHref);
        //logLink(depthLevel, absHref);
        if (processIfProductPage(absHref)) {
            processProduct(absHref);
            queue.add(new Pair<>(depthLevel + 1, absHref));
        }
    }

    /**
     * Connects to a URL if it leads to a product page and processes it as such.
     *
     * @param absHref The absolute URL of the link.
     * @throws IOException If an I/O error occurs.
     */
    private boolean processIfProductPage(String absHref) throws IOException {
        System.out.println("Checking if product page: " + absHref);
        if (!isProductPage(absHref)) {
            System.out.println("Not a product page: " + absHref);
            return false;
        }
        System.out.println("Product page found: " + absHref);
        Document doc = ShopScraper.connectToURL(absHref);
        if (!ShopScraper.checkATagsForHref(doc)) {
            System.out.println("Useless product: " + absHref);
            return false;
        }
        System.out.println("Product found !!! " + absHref);
        return true;
    }


    /**
     * Determines if a URL should be processed based on its validity and visited status.
     *
     * @param absHref The absolute URL to be checked.
     * @return true if the URL is valid and has not been visited; false otherwise.
     */
    private boolean shouldProcessLink(String absHref) {
        boolean shouldProcess = ShopScraper.isValidURL(absHref) && !visitedURLs.contains(absHref);
        visitedURLs.add(absHref);
        return shouldProcess;
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
        return absHref.matches(".*/p#?$");
    }

    /**
     * Processes the product page, either updating the existing product or inserting a new one.
     *
     * @param absHref The absolute URL of the product page.
     */
    void processProduct(String absHref) {
        try {
            Optional<Product> optionalProduct = productScraper.getProductDetails(shopScraper, absHref);
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                Product existingProduct = pto.getProductByName(product.getName());

                if (existingProduct != null) {
                    processExistingProduct(product, existingProduct);
                } else {
                    insertNewProduct(product);
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex, () -> "An error occurred while processing the product: " + absHref);
        }
    }

    /**
     * Handles the product if it exists in the database.
     *
     * @param product         The product fetched from the website.
     * @param existingProduct The product fetched from the database.
     */
    private void processExistingProduct(Product product, Product existingProduct) {
        try {
            LocalDateTime lastModified = existingProduct.getLastModified();
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Paris"));
            long hoursElapsed = Duration.between(lastModified, now).toHours();

            if (hoursElapsed >= 12) {
                updateProduct(product);
            } else {
                LogProductDetails.logProductNotEligibleForUpdate(product.getName());
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex, () -> "An error occurred while processing an existing product: " + product.getName());
        }
    }

    /**
     * Inserts a new product to the database.
     *
     * @param product The product to be inserted.
     */
    private void insertNewProduct(Product product) {
        try {
            pto.insertProduct(product);
            LogProductDetails.logProductInsertion(product.getName());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex, () -> "An error occurred while inserting a new product: " + product.getName());
        }
    }

    /**
     * Updates the existing product in the database.
     *
     * @param product The product to be updated.
     */
    private void updateProduct(Product product) {
        try {
            pto.updateProduct(product);
            LogProductDetails.logProductUpdate(product.getName());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex, () -> "An error occurred while updating a product: " + product.getName());
        }
    }
}