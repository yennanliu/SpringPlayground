package com.yen.ShoppingCart.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.yen.ShoppingCart.config.RedisConfig;
import com.yen.ShoppingCart.model.Category;
import com.yen.ShoppingCart.repository.Categoryrepository;
import com.yen.ShoppingCart.service.CategoryService;
import java.util.List;
import java.util.Optional;
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
 * Integration test for CategoryService caching.
 * Uses ConcurrentMapCacheManager — no Redis or MySQL required.
 *
 * Key behaviour under test:
 *  - listCategories()  → cached after first call; repo not hit on subsequent calls
 *  - createCategory()  → evicts the category list cache
 *  - updateCategory()  → evicts the category list cache
 */
@SpringJUnitConfig(CategoryServiceCacheTest.TestConfig.class)
class CategoryServiceCacheTest {

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
        public Categoryrepository categoryrepository() {
            return Mockito.mock(Categoryrepository.class);
        }

        @Bean
        public CategoryService categoryService() {
            return new CategoryService();
        }
    }

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private Categoryrepository categoryrepository;

    @Autowired
    private CacheManager cacheManager;

    private Category cat1;
    private Category cat2;
    private Category cat3;

    @BeforeEach
    void setUp() {
        Mockito.reset(categoryrepository);
        cacheManager.getCache(RedisConfig.CACHE_CATEGORIES).clear();

        cat1 = new Category(1, "Electronics", "Gadgets", "img1.png");
        cat2 = new Category(2, "Clothing",    "Apparel", "img2.png");
        cat3 = new Category(3, "Books",       "Reading", "img3.png");
    }

    @Test
    void listCategories_secondCall_shouldHitCacheNotRepo() {
        when(categoryrepository.findAll()).thenReturn(List.of(cat1, cat2, cat3));

        List<Category> first  = categoryService.listCategories();
        List<Category> second = categoryService.listCategories();

        assertEquals(3, first.size());
        assertEquals(3, second.size());
        // repo must have been called only once
        verify(categoryrepository, times(1)).findAll();
    }

    @Test
    void createCategory_shouldEvictCategoryCache() {
        when(categoryrepository.findAll()).thenReturn(List.of(cat1, cat2));

        // prime the cache
        categoryService.listCategories();
        verify(categoryrepository, times(1)).findAll();

        // create evicts the cache
        categoryService.createCategory(cat3);
        verify(categoryrepository, times(1)).save(cat3);

        // next call must go to repo
        when(categoryrepository.findAll()).thenReturn(List.of(cat1, cat2, cat3));
        List<Category> afterCreate = categoryService.listCategories();
        verify(categoryrepository, times(2)).findAll();
        assertEquals(3, afterCreate.size());
    }

    @Test
    void updateCategory_shouldEvictCategoryCache() {
        when(categoryrepository.findAll()).thenReturn(List.of(cat1, cat2));
        when(categoryrepository.findById(1)).thenReturn(Optional.of(cat1));

        // prime the cache
        categoryService.listCategories();
        verify(categoryrepository, times(1)).findAll();

        // update evicts the cache
        Category updated = new Category("Electronics Pro", "Updated Gadgets", "img_new.png");
        categoryService.updateCategory(1, updated);

        // next call must go to repo
        categoryService.listCategories();
        verify(categoryrepository, times(2)).findAll();
    }

    @Test
    void listCategories_emptyList_shouldBeCached() {
        when(categoryrepository.findAll()).thenReturn(List.of());

        List<Category> first  = categoryService.listCategories();
        List<Category> second = categoryService.listCategories();

        assertTrue(first.isEmpty());
        assertTrue(second.isEmpty());
        verify(categoryrepository, times(1)).findAll();
    }

    @Test
    void createCategory_multipleCreates_eachEvictsCache() {
        when(categoryrepository.findAll()).thenReturn(List.of(cat1));

        categoryService.listCategories();           // prime → repo call #1
        categoryService.createCategory(cat2);       // evict
        categoryService.listCategories();           // miss  → repo call #2
        categoryService.createCategory(cat3);       // evict
        categoryService.listCategories();           // miss  → repo call #3

        verify(categoryrepository, times(3)).findAll();
    }
}
