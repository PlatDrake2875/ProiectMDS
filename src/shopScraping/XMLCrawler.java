package shopScraping;

import database.ProductTableOperations;
import org.jsoup.nodes.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Class responsible for crawling through XML files to retrieve product data.
 */
public class XMLCrawler extends ProductCrawler {
    private static final Logger LOGGER = new AppLogger(XMLCrawler.class).getLogger();
    private static final Map<String, List<String>> xmlCache = new ConcurrentHashMap<>();
    private final Set<String> visitedURLs = ConcurrentHashMap.newKeySet();
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    // Limit the number of concurrent requests to 10 (Auchan's servers are slow and we don't wanna bully them)
    private static final Semaphore SEMAPHORE = new Semaphore(10);


    /**
     * Constructs a new instance of XMLCrawler.
     * @param shopScraper An instance of ShopScraper which is used to scrape data from online shops.
     * @param productScraper An instance of ProductScraper which is used to scrape data about products.
     * @param pto An instance of ProductTableOperations which provides methods to perform operations on the product database table.
     */
    public XMLCrawler(ShopScraper shopScraper, ProductScraper productScraper, ProductTableOperations pto) {
        super(shopScraper, productScraper, pto);
    }

    /**
     * Starts the process of retrieving product data from Auchan by processing a list of XML files.
     */
    @Override
    public void getProductsAuchan() {
        var xmlFileURLs = generateXMLFileURLs();
        xmlFileURLs.parallelStream().forEach(this::processXMLFile);
    }

    /**
     * Generates URLs for a set of XML files.
     * @return A list of URLs for the XML files.
     */
    private List<String> generateXMLFileURLs() {
        return IntStream.rangeClosed(0, 11)
                .mapToObj(i -> "https://www.auchan.ro/sitemap/product-" + i + ".xml")
                .toList();
    }


    /**
     * Processes a single XML file to extract product data.
     * @param xmlFileURL The URL of the XML file to be processed.
     */
    private void processXMLFile(String xmlFileURL) {
        try {
            System.out.println("Processing XML file: " + xmlFileURL);
            NodeList nList = getXMLFileNodes(xmlFileURL);
            processNodes(nList);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            handleException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves all 'loc' nodes from an XML file.
     * @param xmlFileURL The URL of the XML file to be processed.
     * @return A NodeList containing all 'loc' nodes from the XML file.
     * @throws Exception if any error occurs during XML file processing.
     */
    private NodeList getXMLFileNodes(String xmlFileURL) throws Exception {
        org.w3c.dom.Document xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFileURL);
        xmlDoc.getDocumentElement().normalize();
        return xmlDoc.getElementsByTagName("loc");
    }

    /**
     * Processes all nodes from a NodeList.
     * @param nList NodeList to be processed.
     */
    private void processNodes(NodeList nList) {
        IntStream.range(0, nList.getLength())
                .mapToObj(nList::item)
                .filter(node -> node.getNodeType() == Node.ELEMENT_NODE)
                .map(this::getNodeTextContent)
                .filter(this::isUrlNotVisited)
                .forEach(this::processProductUrl);
    }

    /**
     * Checks if a URL has already been visited.
     * @param url The URL to check.
     * @return A boolean value indicating whether the URL has been visited or not.
     */
    private boolean isUrlNotVisited(String url) {
        return !visitedURLs.contains(url);
    }

    /**
     * Retrieves the text content of a Node.
     * @param node The Node from which to retrieve the text content.
     * @return A string containing the text content of the Node.
     */
    private String getNodeTextContent(Node node) {
        Element eElement = (Element) node;
        return eElement.getTextContent();
    }

    /**
     * Processes a product URL. This method attempts to acquire a semaphore permit before submitting the task for execution.
     * @param productUrl The URL of the product to be processed.
     */
    private void processProductUrl(String productUrl) {
        if (SEMAPHORE.tryAcquire()) {  // Sumbit a task only if a semaphore permit is available!
            executor.submit(() -> {
                try {
                    Document doc = ShopScraper.connectToURL(productUrl);
                    visitedURLs.add(productUrl);
                    if (ShopScraper.checkATagsForHref(doc)) {
                        processProduct(productUrl);
                    }
                } catch (IOException e) {
                    handleException(e);
                } finally {
                    SEMAPHORE.release();
                }
            });
        } else {
            LOGGER.log(Level.WARNING, "Could not acquire semaphore permit for URL: {0}", productUrl);
        }
    }



    /**
     * Retrieves all 'loc' links from all XML files.
     * @return A list of all 'loc' links from all XML files.
     */
    public List<String> getAllLocLinks() {
        return generateXMLFileURLs().parallelStream()
                .flatMap(this::getLocLinksFromXML)
                .toList();
    }


    /**
     * Retrieves all 'loc' links from a single XML file.
     * @param xmlFileURL The URL of the XML file from which to retrieve 'loc' links.
     * @return A Stream of 'loc' links from the XML file.
     */
    private Stream<String> getLocLinksFromXML(String xmlFileURL) {
        return xmlCache.computeIfAbsent(xmlFileURL, this::fetchAndParseXML)
                .parallelStream();
    }

    /**
     * Fetches an XML file and parses it to get 'loc' links.
     * @param xmlFileURL The URL of the XML file to be fetched and parsed.
     * @return A list of 'loc' links from the XML file.
     */
    private List<String> fetchAndParseXML(String xmlFileURL) {
        try {
            var nList = getXMLFileNodes(xmlFileURL);
            return getLocLinks(nList);
        } catch (Exception e) {
            handleException(e);
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves all 'loc' links from a NodeList.
     * @param nList The NodeList from which to retrieve 'loc' links.
     * @return A list of 'loc' links from the NodeList.
     */
    private List<String> getLocLinks(NodeList nList) {
        return IntStream.range(0, nList.getLength()).boxed().parallel()
                .map(nList::item)
                .filter(node -> node.getNodeType() == Node.ELEMENT_NODE)
                .map(this::getNodeTextContent)
                .toList();
    }

    /**
     * Handles an Exception by logging the error.
     *
     * @param e the Exception that occurred
     */
    private void handleException(Exception e) {
        // Log the error
        LOGGER.log(Level.SEVERE, "An error occurred while trying to process a URL or XML file: {0}", e.getMessage());
    }
}
