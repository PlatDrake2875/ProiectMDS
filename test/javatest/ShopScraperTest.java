package javatest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.testng.annotations.Test;
import Proiect.MDS.web.shopScraping.ShopScraper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.testng.AssertJUnit.*;


public class ShopScraperTest {
    @Test
    void isValidURL() {
        assertTrue(ShopScraper.isValidURL("https://www.example.com"));
        assertFalse(ShopScraper.isValidURL("htp://malformed.url"));
        assertFalse(ShopScraper.isValidURL("malformed"));
    }

    @Test
    void containsTargetHref() {
        assertTrue(ShopScraper.containsTargetHref("/brutarie,-cofetarie,-gastro/d"));
        assertFalse(ShopScraper.containsTargetHref("/non-target/d"));
    }

    @Test
    void getAuchanProductPriceToString() {
        String html = "<span>23,99</span><span>24,99</span><span>something else</span>";
        Document doc = Jsoup.parse(html);
        Elements price = doc.select("span");

        String expectedPrice = "23.9924.99";
        String actualPrice = ShopScraper.getAuchanProductPriceToString(price);

        assertEquals(expectedPrice, actualPrice);
    }
}
