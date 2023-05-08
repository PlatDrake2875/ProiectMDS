package shopScraping;


import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Triplet;
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

public class ShopScraper {
    private static final HashSet<String> visitedURLs = new HashSet<>();

    public static void main(String[] args) throws IOException {
        //String url = "https://www.auchan.ro/brutarie-cofetarie-gastro/c";
       /* try {
            getProductsAuchan();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        getProductAuchan("https://www.auchan.ro/iaurt-grecesc-olympus-150-g/p");
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
            Elements category = doc.select(".vtex-breadcrumb-1-x-link.vtex-breadcrumb-1-x-link--productBreadcrumb.vtex-breadcrumb-1-x-link--3.vtex-breadcrumb-1-x-link--productBreadcrumb--3.dib.pv1.link.ph2.c-muted-2.hover-c-link");
            Elements price = doc.select(".vtex-product-price-1-x-currencyContainer--pdp").select("span");
            String priceString = price.get(1).text() + "." + price.get(3).text();
            Elements name = doc.select(".vtex-store-components-3-x-productBrand--productPage").select("span");
            Elements properties = doc.select(".vtex-product-specifications-1-x-specificationValue.vtex-product-specifications-1-x-specificationValue--first.vtex-product-specifications-1-x-specificationValue--last");
            return new Quartet<>(category.text(), name.text(), Double.parseDouble(priceString), Double.parseDouble((properties.get(3).text())));
        } catch (IOException e) {
            return null;
        }
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
