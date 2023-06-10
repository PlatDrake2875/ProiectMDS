package shopScraping;

import org.javatuples.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class responsible for crawling through webpages to retrieve product data.
 */
public class ProductCrawler {
    private static final Set<String> visitedURLs = new HashSet<>();
    private static final Logger LOGGER = Logger.getLogger(ProductCrawler.class.getName());
    private static final String[] crawlingURLs = new String[]{
            "https://www.auchan.ro/brutarie-cofetarie-gastro/c",
            "https://www.auchan.ro/bauturi-si-tutun/c",
            "https://www.auchan.ro/bacanie/c",
            "https://www.auchan.ro/lactate-carne-mezeluri---peste/c",
            "https://www.auchan.ro/fructe-si-legume/c"
    };

    private final ShopScraper shopScraper;
    private final ProductScraper productScraper;

    public ProductCrawler(ShopScraper shopScraper, ProductScraper productScraper) {
        this.shopScraper = shopScraper;
        this.productScraper = productScraper;
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
     * @param queue URL queue
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
     * Process each link, get product details if it is a product URL, and add it to the queue and visitedURLs set.
     *
     * @param queue URL queue
     * @param depthLevel depth level
     * @param links elements containing links
     */
    private void processLinks(Queue<Pair<Integer, String>> queue, int depthLevel, Elements links) throws IOException {
        for (Element link : links) {
            String absHref = link.attr("abs:href");

            if (ShopScraper.isValidURL(absHref) && !visitedURLs.contains(absHref)) {
                LOGGER.log(Level.INFO, "{0} {1} ", new String[]{String.valueOf(depthLevel), absHref});

                if (absHref.endsWith("/p") || absHref.endsWith("/p#")) {
                    productScraper.getProductDetails(shopScraper, absHref);
                }
                visitedURLs.add(absHref);
                queue.add(new Pair<>(depthLevel + 1, absHref));
            }
        }
    }
}
