package src;

import DataModel.Product;
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
    }
}