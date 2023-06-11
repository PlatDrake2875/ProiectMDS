package shopScraping;

import DataModel.Product;
import database.ProductTableOperations;
import org.javatuples.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class responsible for crawling through webpages to retrieve product data.
 */
public class ProductCrawler {

    private static final Set<String> visitedURLs = ConcurrentHashMap.newKeySet();
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
            visitedURLs.add(urlPair.getValue1());
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
            if (ShopScraper.isValidURL(url)) {
                Document doc = ShopScraper.connectToURL(url);
                Elements links = doc.select("a[href]");
                processLinks(queue, urlPair.getValue0(), links);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while connecting to URL: {0}", e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.log(Level.SEVERE, "The processURL operation was interrupted: {0}", e.getMessage());
        }
    }


    /**
     * Processes a collection of links concurrently, determining if each link is a product page
     * and either updating the existing product or inserting a new one to the database.
     *
     * @param queue      The queue of URLs yet to be processed.
     * @param depthLevel The depth level of the crawl.
     * @param links      The collection of links to process.
     * @throws InterruptedException if there is an interruption while waiting for tasks to finish.
     */
    private void processLinks(Queue<Pair<Integer, String>> queue, int depthLevel, Elements links) throws InterruptedException, IOException {
        List<Callable<Void>> tasks = new ArrayList<>();

        for (Element link : links) {
            String absHref = link.attr("abs:href");

            System.out.println(absHref);

            if (shouldProcessLink(absHref)) {
                visitedURLs.add(absHref);
                tasks.add(() -> {
                    processLink(queue, depthLevel, absHref);
                    return null;
                });
            }
        }

        try (ExecutorService executorService = Executors.newFixedThreadPool(12)) {
            executorService.invokeAll(tasks);
            executorService.shutdown();
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        }
    }


    /**
     * Processes a link by logging it, checking if it leads to a product page, and adding it to the visited URLs.
     *
     * @param queue The queue to which the link will be added.
     * @param depthLevel The depth level of the link in the crawl hierarchy.
     * @param absHref The absolute URL of the link.
     * @throws IOException If an I/O error occurs.
     */
    private void processLink(Queue<Pair<Integer, String>> queue, int depthLevel, String absHref) throws IOException {
        visitedURLs.add(absHref);
        logLink(depthLevel, absHref);
        processIfProductPage(absHref);
        addVisitedURL(queue, depthLevel, absHref);
    }


    /**
     * Connects to a URL if it leads to a product page and processes it as such.
     *
     * @param absHref The absolute URL of the link.
     * @throws IOException If an I/O error occurs.
     */
    private void processIfProductPage(String absHref) throws IOException {
        if (!isProductPage(absHref)) {
            return;
        }

        Document doc = ShopScraper.connectToURL(absHref);
        if (!ShopScraper.checkATagsForHref(doc)) {
            return;
        }

        System.out.println("Product found !!! " + absHref);
        processProduct(absHref);
    }


    /**
     * Adds a URL to the list of visited URLs and to the crawl queue.
     *
     * @param queue The queue to which the link will be added.
     * @param depthLevel The depth level of the link in the crawl hierarchy.
     * @param absHref The absolute URL of the link.
     */
    private void addVisitedURL(Queue<Pair<Integer, String>> queue, int depthLevel, String absHref) {
        if (!visitedURLs.contains(absHref)) {
            queue.add(new Pair<>(depthLevel + 1, absHref));
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
        Optional<Product> optionalProduct = productScraper.getProductDetails(shopScraper, absHref);

        // Check if the product is not null before processing
        optionalProduct.ifPresent(product -> {
            Product existingProduct = pto.getProductByName(product.getName());

            if (existingProduct != null) {
                processExistingProduct(product, existingProduct);
            } else {
                insertNewProduct(product);
            }
        });
    }

    /**
     * Handles the product if it exists in the database.
     *
     * @param product         The product fetched from the website.
     * @param existingProduct The product fetched from the database.
     */
    private void processExistingProduct(Product product, Product existingProduct) {
        LocalDateTime lastModified = existingProduct.getLastModified();
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Paris"));
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
        pto.insertProduct(product);
        LogProductDetails.logProductInsertion(product.getName());
    }

    /**
     * Updates the existing product in the database.
     *
     * @param product The product to be updated.
     */
    private void updateProduct(Product product) {
        pto.updateProduct(product);
        LogProductDetails.logProductUpdate(product.getName());
    }
}
