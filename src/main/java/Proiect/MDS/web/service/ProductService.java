package Proiect.MDS.web.service;

import Proiect.MDS.web.dto.ProductDto;
import Proiect.MDS.web.dto.RecipeDto;
import Proiect.MDS.web.models.Product;
import Proiect.MDS.web.models.Recipe;

import java.util.List;

public interface ProductService {
    Product getProductById(int id);

    List<ProductDto> getAllProducts();
}
