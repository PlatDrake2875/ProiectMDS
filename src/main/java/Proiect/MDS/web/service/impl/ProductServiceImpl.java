package Proiect.MDS.web.service.impl;

import Proiect.MDS.web.database.Database;
import Proiect.MDS.web.database.ProductTableOperations;
import Proiect.MDS.web.dto.ProductDto;
import Proiect.MDS.web.dto.RecipeDto;
import Proiect.MDS.web.models.Product;
import Proiect.MDS.web.models.Recipe;
import Proiect.MDS.web.repository.ProductRepository;
import Proiect.MDS.web.repository.RecipeRepository;
import Proiect.MDS.web.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    private ProductDto ProductToDto(Product product) {
        ProductDto productDto = ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build();

        return productDto;
    }


    @Override
    public Product getProductById(int id) {
        ProductTableOperations pto = new ProductTableOperations(new Database("jdbc:mysql://localhost/dbProducts", "ioana", "ioana7"));
        return pto.getProductByColumnValue("id", Integer.toString(id));
    }

    @Override
    public List<ProductDto> getAllProducts() {
        ProductTableOperations pto = new ProductTableOperations(new Database("jdbc:mysql://localhost/dbProducts", "ioana", "ioana7"));
        List<Product> products = pto.getAllProducts();
        return products.stream().map((product) -> ProductToDto(product)).collect(Collectors.toList());

    }
}
