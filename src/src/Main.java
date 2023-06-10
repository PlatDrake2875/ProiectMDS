package src;

import shopScraping.ProductScraper;
import shopScraping.ShopScraper;

import java.io.IOException;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        ShopScraper shop = new ShopScraper();
        ProductScraper auchan = new ProductScraper();

        Map<String, String> product = auchan.getProductDetails(shop, "https://www.auchan.ro/branza-de-vaca-cottage-light-olympus-2-grasime-180-g/p");

        for(Map.Entry<String,String> entry : product.entrySet())
        {
            System.out.println(entry.getKey() + " ||||| " + entry.getValue());
        }

    }
}