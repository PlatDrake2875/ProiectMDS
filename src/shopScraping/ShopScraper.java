package shopScraping;


import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShopScraper {
    private static final HashSet<String> visitedURLs = new HashSet<>();
    private static final Logger LOGGER = Logger.getLogger(ShopScraper.class.getName());

    public static void main(String[] args) throws IOException {
        //String url = "https://www.auchan.ro/brutarie-cofetarie-gastro/c";
        /*try {
            getProductsAuchan();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
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

    static Quartet<String, String, Double, Double> getProductAuchan(String urlProduct) {
        try {
            Document doc = Jsoup.connect(urlProduct).get();
            Elements category = getAuchanProductCategory(doc);
            Elements name = getAuchanProductName(doc);
            Elements price = getAuchanProductPrice(doc);
            String priceString = getAuchanProductPriceToString(price);
            Elements properties = getAuchanProductProperties(doc);

            LOGGER.log(Level.INFO, "categorie: {0}", category.text());
            LOGGER.log(Level.INFO, "pret: {0}", priceString);
            LOGGER.log(Level.INFO, "nume: {0}", name.text());
            LOGGER.log(Level.INFO, "proprietati:");

            for (Element element : properties) {
                LOGGER.log(Level.INFO, element.attr("data-specification-name") + " " + element.attr("data-specification-value"));
            }
            return null;
            // return new Quartet<>(category.text(), name.text(), Double.parseDouble(priceString), Double.parseDouble((properties.get(2).text())));
        } catch (IOException e) {
            return null;
        }
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


    private static void getProductsAuchan() throws IOException {
        final String[] crawlingURLs = new String[]{"https://www.auchan.ro/brutarie-cofetarie-gastro/c", "https://www.auchan.ro/bauturi-si-tutun/c", "https://www.auchan.ro/bacanie/c", "https://www.auchan.ro/lactate-carne-mezeluri---peste/c", "https://www.auchan.ro/fructe-si-legume/c"};

        try (FileWriter crawledURLs = new FileWriter("CrawledURLS.txt")) {
            Queue<Pair<Integer, String>> queue = new LinkedList<>();
            for (String url : crawlingURLs) {
                queue.add(new Pair<>(1, url));
                visitedURLs.add(url);
            }

            while (!queue.isEmpty()) {
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
                        System.out.println(depthLevel + " " + absHref);
                        if (absHref.endsWith("/p") || absHref.endsWith(("/p#"))) {
                            getProductAuchan(absHref);
                        }
                        crawledURLs.write(depthLevel + " " + absHref + '\n');
                        visitedURLs.add(absHref);
                        queue.add(new Pair<>(depthLevel + 1, absHref));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while crawling URLs: " + e.getMessage());
        }
    }
}
