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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
    private final List<String> visitedURLs = new ArrayList<>();

    public XMLCrawler(ShopScraper shopScraper, ProductScraper productScraper, ProductTableOperations pto) {
        super(shopScraper, productScraper, pto);
    }

    /**
     * Method to start the product crawling process.
     */
    @Override
    public void getProductsAuchan() {
        processXMLFiles(generateXMLFileURLs());
    }

    /**
     * Generates the list of XML file URLs.
     *
     * @return a List containing the XML file URLs
     */
    private List<String> generateXMLFileURLs() {
        return IntStream.rangeClosed(0, 11)
                .mapToObj(i -> "https://www.auchan.ro/sitemap/product-" + i + ".xml")
                .toList();
    }

    /**
     * Process each XML file.
     *
     * @param xmlFileURLs the URLs of XML files to process
     */
    private void processXMLFiles(List<String> xmlFileURLs) {
        xmlFileURLs.forEach(this::processXMLFile);
    }

    /**
     * Process a single XML file.
     *
     * @param xmlFileURL the URL of XML file to process
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
     * Retrieves all nodes from an XML file.
     *
     * @param xmlFileURL the URL of XML file to process
     * @return NodeList containing all nodes from the XML file
     * @throws Exception if any error occurs during XML file processing
     */
    private NodeList getXMLFileNodes(String xmlFileURL) throws Exception {
        org.w3c.dom.Document xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFileURL);
        xmlDoc.getDocumentElement().normalize();
        return xmlDoc.getElementsByTagName("loc");
    }

    /**
     * Process all nodes from a NodeList.
     *
     * @param nList NodeList to process
     */
    private void processNodes(NodeList nList) {
        IntStream.range(0, nList.getLength())
                .mapToObj(nList::item)
                .filter(node -> node.getNodeType() == Node.ELEMENT_NODE)
                .map(this::getNodeTextContent)
                .filter(this::isUrlNotVisited)
                .forEach(this::processProductUrl);
    }

    /***
     * Checks if a URL has not been visited yet.
     * @param url the URL to check
     * @return true if the URL has not been visited yet, false otherwise
     */
    private boolean isUrlNotVisited(String url) {
        return !visitedURLs.contains(url);
    }

    /**
     * Retrieves the text content of a Node.
     *
     * @param node Node to get the text content from
     * @return String containing the text content of the Node
     */
    private String getNodeTextContent(Node node) {
        Element eElement = (Element) node;
        return eElement.getTextContent();
    }

    /**
     * Processes a product URL.
     *
     * @param productUrl the URL to process
     */
    private void processProductUrl(String productUrl) {
        try {
            Document doc = ShopScraper.connectToURL(productUrl);
            visitedURLs.add(productUrl);
            if (ShopScraper.checkATagsForHref(doc)) {
                processProduct(productUrl);
            }
        } catch (IOException e) {
            handleException(e);
        }
    }


    /**
     * Method to retrieve all "loc" links from all XML files.
     *
     * @return a List of all "loc" links from all XML files.
     */
    public List<String> getAllLocLinks() {
        List<String> xmlFileURLs = generateXMLFileURLs();
        return xmlFileURLs.parallelStream()
                .flatMap(this::getLocLinksFromXML)
                .toList();
    }


    /**
     * Gets all "loc" links from a single XML file.
     *
     * @param xmlFileURL The URL of the XML file to get "loc" links from.
     * @return a Stream of "loc" links from the XML file.
     */
    private Stream<String> getLocLinksFromXML(String xmlFileURL) {
        return xmlCache.computeIfAbsent(xmlFileURL, this::fetchAndParseXML)
                .stream();
    }

    /**
     * Fetches and parses the XML file to get "loc" links.
     *
     * @param xmlFileURL The URL of the XML file to fetch and parse.
     * @return a List of "loc" links from the XML file.
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
     * Gets all "loc" links from a NodeList.
     * @param nList The NodeList to get "loc" links from.
     * @return a List of "loc" links from the NodeList.
     */
    private List<String> getLocLinks(NodeList nList) {
        return IntStream.range(0, nList.getLength())
                .mapToObj(nList::item)
                .filter(node -> node.getNodeType() == Node.ELEMENT_NODE)
                .map(this::getNodeTextContent)
                .collect(Collectors.toList());
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
