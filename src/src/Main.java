package src;

import database.Database;
import database.ProductJsonOperations;
import database.ProductTableOperations;
import org.jsoup.nodes.Document;
import shopScraping.ProductCrawler;
import shopScraping.ProductScraper;
import shopScraping.ShopScraper;
import shopScraping.XMLCrawler;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ShopScraper shop = new ShopScraper();
        ProductScraper auchan = new ProductScraper();
        Database db = new Database("jdbc:mysql://localhost/dbProducts", "root", "2875");

        demo(shop, auchan, db);
    }

    public static void demo(ShopScraper shop, ProductScraper auchan, Database db) throws IOException {
        ProductTableOperations pto = new ProductTableOperations(db);
        ProductCrawler crawler = new ProductCrawler(shop, auchan, pto);

        Document doc = ShopScraper.connectToURL("https://www.auchan.ro/vin-alb-sec-vartely-feteasca-regala-riesling-0-75-l/p");

        //System.out.println(ShopScraper.getAuchanProductPrice(doc));
        //System.out.println(ShopScraper.getAuchanProductPriceToString(ShopScraper.getAuchanProductPrice(doc)));

        //crawlProducts(crawler);

        crawlXMLFiles(shop, auchan, pto);
        printProductsByCriteria(pto);
        exportProductsToJson(pto);
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
