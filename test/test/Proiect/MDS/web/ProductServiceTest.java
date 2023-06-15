//package test.test.Proiect.MDS.web;
//
//import Proiect.MDS.web.dto.ProductDto;
//import Proiect.MDS.web.models.Product;
//import Proiect.MDS.web.repository.ProductRepository;
//import Proiect.MDS.web.service.ProductService;
//import Proiect.MDS.web.service.impl.ProductServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.mockito.Mockito.when;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class ProductServiceTest {
//    private ProductService service;
//    @Mock
//    private ProductRepository repository;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        service = new ProductServiceImpl(repository);
//    }
//
//    @Test
//    public void testGetProductById() {
//        int id = 1;
//        Product product = new Product();
//        product.setId(id);
//        product.setName("test");
//        product.setPrice(BigDecimal.TEN);
//
//        when(repository.getProductById(id)).thenReturn(product);
//
//        Product actualProduct = service.getProductById(id);
//        assertEquals(product, actualProduct);
//
//    }
//
//}
