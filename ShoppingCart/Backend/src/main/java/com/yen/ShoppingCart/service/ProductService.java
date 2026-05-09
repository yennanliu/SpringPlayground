package com.yen.ShoppingCart.service;

import com.yen.ShoppingCart.config.RedisConfig;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Approach 3: evict the product list cache whenever a product is added
    @CacheEvict(value = RedisConfig.CACHE_PRODUCTS, allEntries = true)
    public void addProduct(ProductDto productDto, Category category) {

        Product product = getProductFromDto(productDto, category);
        productRepository.save(product);
    }

    public static Product getProductFromDto(ProductDto productDto, Category category) {

        Product product = new Product();
        product.setCategory(category);
        product.setDescription(productDto.getDescription());
        product.setImageURL(productDto.getImageURL());
        product.setPrice(productDto.getPrice());
        product.setName(productDto.getName());
        return product;
    }

    // Approach 3+5: cached read; routes to replica when replica is enabled
    @Transactional(readOnly = true)
    @Cacheable(value = RedisConfig.CACHE_PRODUCTS, key = "'all'")
    public List<ProductDto> listProducts() {

        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            productDtos.add(getDtoFromProduct(product));
        }
        return productDtos;
    }

    public static ProductDto getDtoFromProduct(Product product) {

        return new ProductDto(product);
    }

    // Approach 3: evict the product list cache whenever a product is updated
    @CacheEvict(value = RedisConfig.CACHE_PRODUCTS, allEntries = true)
    public void updateProduct(Integer productID, ProductDto productDto, Category category) {

        Product product = getProductFromDto(productDto, category);
        product.setId(productID);
        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Product getProductById(Integer productId) throws ProductNotExistException {

        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (!optionalProduct.isPresent()) {
            throw new ProductNotExistException("Product id is invalid " + productId);
        }
        return optionalProduct.get();
    }

    @Transactional(readOnly = true)
    public List<Product> searchProducts(String query) {

        log.info(">>> (searchProducts) query = {}", query);
        return productRepository.searchProductsByName(query);
    }
}
