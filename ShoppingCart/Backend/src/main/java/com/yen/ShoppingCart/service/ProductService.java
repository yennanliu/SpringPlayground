package com.yen.ShoppingCart.service;

import com.yen.ShoppingCart.exception.ProductNotExistException;
import com.yen.ShoppingCart.model.Category;
import com.yen.ShoppingCart.model.Product;
import com.yen.ShoppingCart.model.dto.product.ProductDto;
import com.yen.ShoppingCart.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void addProduct(ProductDto productDto, Category category) {

        Product product = getProductFromDto(productDto, category);
        productRepository.save(product);
    }

    // local method
    public static Product getProductFromDto(ProductDto productDto, Category category) {

        Product product = new Product();
        product.setCategory(category);
        product.setDescription(productDto.getDescription());
        product.setImageURL(productDto.getImageURL());
        product.setPrice(productDto.getPrice());
        product.setName(productDto.getName());
        return product;
    }

    public List<ProductDto> listProducts() {

        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();
        for(Product product : products) {
            ProductDto productDto = getDtoFromProduct(product);
            productDtos.add(productDto);
        }
        return productDtos;
    }

    public static ProductDto getDtoFromProduct(Product product) {

        ProductDto productDto = new ProductDto(product);
        return productDto;
    }

    public void updateProduct(Integer productID, ProductDto productDto, Category category) {

        Product product = getProductFromDto(productDto, category);
        product.setId(productID);
        productRepository.save(product);
    }

    public Product getProductById(Integer productId) throws ProductNotExistException {

        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (!optionalProduct.isPresent()){
            throw new ProductNotExistException("Product id is invalid " + productId);
        }
        return optionalProduct.get();
    }

    public List<Product> searchProducts(String query) {

        log.info(">>> (searchProducts) query = {}", query);
        List<Product> products = productRepository.searchProductsByName(query);
        return productRepository.searchProductsByName(query);
    }
}

