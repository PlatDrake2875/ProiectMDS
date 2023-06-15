package javatest;

import DataModel.Product;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import shopScraping.scrape.ProductScraper;
import shopScraping.scrape.ShopScraper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ProductScraperTest {
    @Mock
    private ShopScraper mockScraper;

    @Mock
    private Document mockDocument;

    @Mock
    private Elements mockElements;

    private ProductScraper productScraper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        productScraper = new ProductScraper();
    }

    @Test
    void testGetProductDetailsSuccessful() throws IOException {
        String urlProduct = "https://www.auchan.ro/suc-natural-de-mere-portocale-si-morcovi-olympus-1-5-l/p";

        System.out.println(mockElements.text());
        when(ShopScraper.connectToURL(anyString())).thenReturn(mockDocument);
        when(mockElements.text()).thenReturn("Test Data");
        when(mockElements.isEmpty()).thenReturn(false);
        when(ShopScraper.getAuchanProductCategory(mockDocument)).thenReturn(mockElements);
        when(ShopScraper.getAuchanProductName(mockDocument)).thenReturn(mockElements);
        when(ShopScraper.getAuchanProductPriceToString(ShopScraper.getAuchanProductPrice(mockDocument))).thenReturn("16.0");
        when(ShopScraper.getAuchanProductProperties(mockDocument)).thenReturn(mockElements);

        Optional<Product> productOptional = productScraper.getProductDetails(mockScraper, urlProduct);

        assertTrue(productOptional.isPresent());
        Product product = productOptional.get();
        assertEquals("Bauturi si Tutun", product.getCategory());
        assertEquals("Bauturi si Tutun", product.getName());
        assertEquals(new BigDecimal("16.0"), product.getPrice());
    }

    @Test
    void testGetProductDetailsFailedNoNameOrPrice() throws IOException {
        String urlProduct = "https://www.auchan.ro/pachet-3-1-gratis-ton-in-sos-de-rosii-calvo-4-x-80-g/p";

        when(ShopScraper.connectToURL(anyString())).thenReturn(mockDocument);
        when(mockElements.text()).thenReturn("");
        when(mockElements.isEmpty()).thenReturn(true);
        when(ShopScraper.getAuchanProductCategory(mockDocument)).thenReturn(mockElements);
        when(ShopScraper.getAuchanProductName(mockDocument)).thenReturn(mockElements);
        when(ShopScraper.getAuchanProductPriceToString(ShopScraper.getAuchanProductPrice(mockDocument))).thenReturn("");

        Optional<Product> productOptional = productScraper.getProductDetails(mockScraper, urlProduct);

        assertTrue(productOptional.isEmpty());
    }

    @Test
   void testGetProductDetailsFailedIOException() throws IOException {
        String urlProduct = "https://www.auchan.ro/vin-alb-sec-vartely-feteasca-regala-riesling-0-75-l/p";

        when(ShopScraper.connectToURL(anyString())).thenThrow(new IOException());

        Optional<Product> productOptional = productScraper.getProductDetails(mockScraper, urlProduct);

        assertTrue(productOptional.isEmpty());
    }
}
