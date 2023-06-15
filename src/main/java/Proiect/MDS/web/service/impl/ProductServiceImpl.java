package Proiect.MDS.web.service.impl;

import Proiect.MDS.web.dto.ProductDto;
import Proiect.MDS.web.models.Product;
import Proiect.MDS.web.repository.ProductRepository;
import Proiect.MDS.web.repository.impl.ProductRepositoryImpl;
import Proiect.MDS.web.service.ProductService;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the ProductService interface.
 * This service is used to interact with the product data within the application.
 */
@Repository
public class ProductServiceImpl implements ProductService {
    private final ProductRepositoryImpl repository;

    /**
     * Constructor with dependency injection via constructor
     * @param repository The ProductRepository to inject.
     */
    public ProductServiceImpl(ProductRepositoryImpl repository) {
        this.repository = repository;
    }

    /**
     * Method to convert a Product entity to a ProductDto
     * @param product The product entity to convert.
     * @return The converted product DTO.
     */
    private ProductDto ProductToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }

    /**
     * Method to get a product by its id.
     * @param id The id of the product to fetch.
     * @return An Optional containing the product with the specified id, or empty if not found.
     */
    @Override
    public Optional<Product> getProductById(int id) {
        return repository.findById(id);
    }

    /**
     * Method to get all products.
     * @return A list of all products in the database, as ProductDtos.
     */
    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = repository.findAll();
        return products.stream().map(this::ProductToDto).toList();
    }
}
