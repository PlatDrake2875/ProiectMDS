package Proiect.MDS.web.src;

import Proiect.MDS.web.database.Database;
import Proiect.MDS.web.database.ProductJsonOperations;
import Proiect.MDS.web.database.ProductTableOperations;
import Proiect.MDS.web.database.RecipeTableOperations;
import Proiect.MDS.web.models.Recipe;
import Proiect.MDS.web.shopScraping.ProductCrawler;
import Proiect.MDS.web.shopScraping.ProductScraper;
import Proiect.MDS.web.shopScraping.ShopScraper;
import Proiect.MDS.web.shopScraping.XMLCrawler;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        Database db = new Database("jdbc:mysql://localhost/dbProducts", "root", "2875");

        // Initialize scrapers
        ShopScraper shop = new ShopScraper();
        ProductScraper auchan = new ProductScraper();

        // Perform the product demo
        //performProductDemo(shop, auchan, db);

        // Perform the recipe demo
        performRecipeDemo(db);
    }

    private static void performProductDemo(ShopScraper shop, ProductScraper auchan, Database db) throws IOException {
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

    private static void performRecipeDemo(Database db) throws SQLException {
        RecipeTableOperations rto = new RecipeTableOperations(db);

        Recipe reteta0 = new Recipe.Builder()
                .id(1)
                .photoURL("https://i.pinimg.com/564x/09/79/c3/0979c344423e73d805f1ad841fe0b50c.jpg")
                .recipeName("Supa de pui")
                .estimatedCookingTime(40)
                .estimatedPreparationTime(10)
                .portionSize(8)
                .products("Pui, Morcovi, Apa, Sare")
                .build();

        Recipe reteta1 = new Recipe.Builder()
                .id(1)
                .photoURL("https://i.pinimg.com/564x/29/84/2d/29842d65620c3d3ab14bc81eea9fe3ea.jpg")
                .recipeName("Clatite")
                .estimatedCookingTime(40)
                .estimatedPreparationTime(10)
                .portionSize(8)
                .products("Lapte, Faina, Oua, Zahar")
                .build();

        Recipe reteta2 = new Recipe.Builder()
                .id(2)
                .photoURL("https://i.pinimg.com/564x/87/5c/ff/875cff184bc8833778a04de8af527406.jpg")
                .recipeName("Curry cu legume")
                .estimatedCookingTime(60)
                .estimatedPreparationTime(10)
                .portionSize(4)
                .products("Orez, Morcovi, Ceapa")
                .build();

        Recipe reteta3 = new Recipe.Builder()
                .id(3)
                .photoURL("https://i.pinimg.com/564x/a0/de/dc/a0dedca226bcbdaadb82958136d48dd5.jpg")
                .recipeName("Inghetata")
                .estimatedCookingTime(45)
                .estimatedPreparationTime(10)
                .portionSize(6)
                .products("Lapte, Zahar, Vanilie")
                .build();


        rto.insertRecipe(reteta0);
        rto.insertRecipe(reteta1);
        rto.insertRecipe(reteta2);
        rto.insertRecipe(reteta3);

        List<Recipe> recipes = rto.getAllRecipes();
        for (Recipe recipe : recipes) {
            System.out.println(recipe);
        }
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
