package src;

import DataModel.Product;
import database.Database;
import shopScraping.ProductCrawler;
import shopScraping.ProductScraper;
import shopScraping.ShopScraper;

import java.io.IOException;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        ShopScraper shop = new ShopScraper();
        ProductScraper auchan = new ProductScraper();

        Product product = auchan.getProductDetails(shop, "https://www.auchan.ro/branza-de-vaca-cottage-light-olympus-2-grasime-180-g/p");

        System.out.println(product);

        Database db = new Database("jdbc:mysql://localhost/dbProducts", "root", "2875");

        ProductCrawler crawler = new ProductCrawler(shop, auchan, db);

        crawler.getProductsAuchan();

        db.printProductsByCriteria("WHERE price < 123.5");
    }
}