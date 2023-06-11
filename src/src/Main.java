package src;

import DataModel.Product;
import database.Database;
import database.ProductJsonOperations;
import database.ProductTableOperations;
import shopScraping.ProductCrawler;
import shopScraping.ProductScraper;
import shopScraping.ShopScraper;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ShopScraper shop = new ShopScraper();
        ProductScraper auchan = new ProductScraper();

        // Get product details
        Product product = auchan.getProductDetails(shop, "https://www.auchan.ro/branza-de-vaca-cottage-light-olympus-2-grasime-180-g/p").orElseThrow();

        System.out.println(product);

        Database db = new Database("jdbc:mysql://localhost/dbProducts", "root", "2875");

        ProductTableOperations pto = new ProductTableOperations(db);

        ProductCrawler crawler = new ProductCrawler(shop, auchan, pto);

        crawler.getProductsAuchan();

        ProductJsonOperations pjo = new ProductJsonOperations(pto);
        pjo.exportProductsToJson("products.json");

        pto.printProductsByCriteria("WHERE price < 12.5");
    }
}
