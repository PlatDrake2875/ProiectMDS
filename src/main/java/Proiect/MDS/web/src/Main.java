package Proiect.MDS.web.src;

import Proiect.MDS.web.database.Database;
import Proiect.MDS.web.database.ProductJsonOperations;
import Proiect.MDS.web.database.ProductTableOperations;
import Proiect.MDS.web.shopScraping.ProductCrawler;
import Proiect.MDS.web.shopScraping.ProductScraper;
import Proiect.MDS.web.shopScraping.ShopScraper;
import Proiect.MDS.web.shopScraping.XMLCrawler;
import org.jsoup.nodes.Document;


import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {


        ShopScraper shop = new ShopScraper();
        ProductScraper auchan = new ProductScraper();
        Database db = new Database("jdbc:mysql://localhost/dbProducts", "ioana", "ioana7");

        demo(shop, auchan, db);
    }

    public static void demo(ShopScraper shop, ProductScraper auchan, Database db) throws IOException {
        ProductTableOperations pto = new ProductTableOperations(db);
        ProductCrawler crawler = new ProductCrawler(shop, auchan, pto);

        Document doc = ShopScraper.connectToURL("https://www.auchan.ro/vin-alb-sec-vartely-feteasca-regala-riesling-0-75-l/p");

        System.out.println(ShopScraper.getAuchanProductPrice(doc));
        System.out.println(ShopScraper.getAuchanProductPriceToString(ShopScraper.getAuchanProductPrice(doc)));

        crawlProducts(crawler);
        exportProductsToJson(pto);

        crawlXMLFiles(shop, auchan, pto);
        printProductsByCriteria(pto);
    }

    private static void crawlProducts(ProductCrawler crawler) {
        crawler.getProductsAuchan();
    }

    private static void exportProductsToJson(ProductTableOperations pto) {
        ProductJsonOperations pjo = new ProductJsonOperations(pto);
        pjo.exportProductsToJson("products.json");
    }

    private static void printProductsByCriteria(ProductTableOperations pto) {
        pto.printProductsByCriteria("price < 12.5");
    }

    private static void crawlXMLFiles(ShopScraper shop, ProductScraper auchan, ProductTableOperations pto) {
        XMLCrawler xmlCrawler = new XMLCrawler(shop, auchan, pto);
        xmlCrawler.getProductsAuchan();
    }
}