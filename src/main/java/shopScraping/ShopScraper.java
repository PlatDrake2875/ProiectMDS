package main.java.shopScraping;

import database.Database;
import org.javatuples.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShopScraper {
    private static final Set<String> visitedURLs = new HashSet<>();
    private static final Logger LOGGER = Logger.getLogger(ShopScraper.class.getName());

    public static void main(String[] args) throws IOException {
        getProductAuchan("https://www.auchan.ro/paine-7-seminte-vel-pitar-700-g/p");
    }

    static boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }

    static Map<String, String> getProductAuchan(String urlProduct) {
        Map<String, String> productProperties = new HashMap<>();

        try {
            Document doc = Jsoup.connect(urlProduct).get();
            Elements category = getAuchanProductCategory(doc);
            Elements name = getAuchanProductName(doc);
            Elements price = getAuchanProductPrice(doc);
            String priceString = getAuchanProductPriceToString(price);
            Elements properties = getAuchanProductProperties(doc);

            logProductDetails(category, name, priceString, properties);

            try (FileWriter productColumnNamesFile = new FileWriter("productColumnNames.txt")) {
                writeProductDetailsToFile(productColumnNamesFile, category, name, price, properties);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }

        return productProperties;
    }

    public static Elements getAuchanProductCategory(Document doc) {
        return doc.select(".vtex-breadcrumb-1-x-link.vtex-breadcrumb-1-x-link--productBreadcrumb.vtex-breadcrumb-1-x-link--3.vtex-breadcrumb-1-x-link--productBreadcrumb--3.dib.pv1.link.ph2.c-muted-2.hover-c-link");
    }

    public static Elements getAuchanProductName(Document doc) {
        return doc.select(".vtex-store-components-3-x-productBrand--productPage").select("span");
    }

    public static Elements getAuchanProductPrice(Document doc) {
        return doc.select(".vtex-product-price-1-x-currencyContainer--pdp").select("span");
    }

    public static String getAuchanProductPriceToString(Elements price) {
        return price.get(1).text() + "." + price.get(3).text();
    }

    public static Elements getAuchanProductProperties(Document doc) {
        return doc.select(".vtex-product-specifications-1-x-specificationValue.vtex-product-specifications-1-x-specificationValue--first.vtex-product-specifications-1-x-specificationValue--last");
    }

    private static void logProductDetails(Elements category, Elements name, String priceString, Elements properties) {
        LOGGER.log(Level.INFO, "categorie: {0}", category.text());
        LOGGER.log(Level.INFO, "pret: {0}", priceString);
        LOGGER.log(Level.INFO, "nume: {0}", name.get(0).text());
        LOGGER.log(Level.INFO, "proprietati:");

        properties.forEach(elem -> LOGGER.log(Level.INFO, "name: {0},value: {1}", new String[]{
                elem.attr("data-specification-name"),
                elem.attr("data-specification-value")
        }));
    }

    private static void writeProductDetailsToFile(FileWriter productColumnNamesFile, Elements category, Elements name, Elements price, Elements properties) throws IOException {
        productColumnNamesFile.write(name.get(0).text() + '\n');
        productColumnNamesFile.write(category.text() + '\n');
        productColumnNamesFile.write(price.get(0).text() + '\n');

        properties.forEach(elem -> {
            try {
                productColumnNamesFile.write(elem.attr("data-specification-name") + ": " + elem.attr("data-specification-value") + '\n');
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


    private static void getProductsAuchan() throws IOException {
        final String[] crawlingURLs = new String[]{"https://www.auchan.ro/brutarie-cofetarie-gastro/c",
                "https://www.auchan.ro/bauturi-si-tutun/c",
                "https://www.auchan.ro/bacanie/c",
                "https://www.auchan.ro/lactate-carne-mezeluri---peste/c",
                "https://www.auchan.ro/fructe-si-legume/c"};

        try (FileWriter crawledURLs = new FileWriter("CrawledURLS.txt")) {
            Queue<Pair<Integer, String>> queue = new LinkedList<>();
            for (String url : crawlingURLs) {
                queue.add(new Pair<>(1, url));
                visitedURLs.add(url);
            }

            //Database db = new Database();
            //boolean OK = db.isUpdateTime();

            /*while (!queue.isEmpty()) {
                int depthLevel = queue.peek().getValue0();
                String url = queue.peek().getValue1();
                queue.remove();

                if (depthLevel >= 5) {
                    continue;
                }

                Document doc = Jsoup.connect(url).get();
                Elements links = doc.select("a[href]");

                for (Element link : links) {
                    String absHref = link.attr("abs:href");

                    if (isValidURL(absHref) && !visitedURLs.contains(absHref)) {
                        LOGGER.log(Level.INFO, "{0} {1} ",
                                new String[]{String.valueOf(depthLevel), absHref});

                        if (absHref.endsWith("/p") || absHref.endsWith(("/p#"))) {
                            getProductAuchan(absHref);
                        }
                        visitedURLs.add(absHref);
                        queue.add(new Pair<>(depthLevel + 1, absHref));
                    }
                }
            }*/

        } catch (IOException e) {
            LOGGER.log(Level.INFO, "An error occurred while crawling URLs: {0}", e.getMessage());
        }
    }

}
