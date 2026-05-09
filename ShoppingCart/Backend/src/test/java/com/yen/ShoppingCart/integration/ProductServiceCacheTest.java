package com.yen.ShoppingCart.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.yen.ShoppingCart.config.RedisConfig;
import com.yen.ShoppingCart.model.Category;
import com.yen.ShoppingCart.model.Product;
import com.yen.ShoppingCart.model.dto.product.ProductDto;
import com.yen.ShoppingCart.repository.ProductRepository;
import com.yen.ShoppingCart.service.ProductService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * Integration test for ProductService caching.
 * Uses ConcurrentMapCacheManager — no Redis or MySQL required.
 *
 * Key behaviour under test:
 *  - listProducts()    → cached after first call; DB not hit on second call
 *  - addProduct()      → evicts the product list cache
 *  - updateProduct()   → evicts the product list cache
 */
@SpringJUnitConfig(ProductServiceCacheTest.TestConfig.class)
class ProductServiceCacheTest {

    @EnableCaching
    @Configuration
    static class TestConfig {

        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager(
                    RedisConfig.CACHE_TOKENS,
                    RedisConfig.CACHE_PRODUCTS,
                    RedisConfig.CACHE_CATEGORIES
            );
        }

        @Bean
        public ProductRepository productRepository() {
            return Mockito.mock(ProductRepository.class);
        }

        @Bean
        public ProductService productService() {
            return new ProductService();
        }
    }

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CacheManager cacheManager;

    private Category category;
    private Product product1;
    private Product product2;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        Mockito.reset(productRepository);
        cacheManager.getCache(RedisConfig.CACHE_PRODUCTS).clear();

        category = new Category(1, "Electronics", "Electronics category", "img.png");
        product1 = new Product("iPhone", "url1", 999.0, "smartphone", category);
        product1.setId(1);
        product2 = new Product("Galaxy", "url2", 899.0, "smartphone", category);
        product2.setId(2);

        productDto = new ProductDto("New Product", "url3", 199.0, "desc", 1);
    }

    @Test
    void listProducts_secondCall_shouldHitCacheNotRepo() {
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        List<ProductDto> first  = productService.listProducts();
        List<ProductDto> second = productService.listProducts();

        assertEquals(2, first.size());
        assertEquals(first.size(), second.size());
        // repo called only once; second result came from cache
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void addProduct_shouldEvictProductCache() {
        when(productRepository.findAll()).thenReturn(List.of(product1));

        // prime the cache
        productService.listProducts();
        verify(productRepository, times(1)).findAll();

        // add a product → cache evicted
        productService.addProduct(productDto, category);
        verify(productRepository, times(1)).save(any());

        // next listProducts must go to repo again
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));
        List<ProductDto> afterAdd = productService.listProducts();
        verify(productRepository, times(2)).findAll();
        assertEquals(2, afterAdd.size());
    }

    @Test
    void updateProduct_shouldEvictProductCache() {
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        // prime the cache
        productService.listProducts();
        verify(productRepository, times(1)).findAll();

        // update a product → cache evicted
        ProductDto updatedDto = new ProductDto("iPhone 16", "url_new", 1099.0, "new model", 1);
        productService.updateProduct(1, updatedDto, category);
        verify(productRepository, times(1)).save(any());

        // next listProducts must go to repo again
        productService.listProducts();
        verify(productRepository, times(2)).findAll();
    }

    @Test
    void listProducts_emptyList_shouldBeCached() {
        when(productRepository.findAll()).thenReturn(List.of());

        List<ProductDto> first  = productService.listProducts();
        List<ProductDto> second = productService.listProducts();

        assertTrue(first.isEmpty());
        assertTrue(second.isEmpty());
        verify(productRepository, times(1)).findAll();
    }
}
