package com.yen.ShoppingCart.service;

import com.yen.ShoppingCart.config.RedisConfig;
import com.yen.ShoppingCart.model.Category;
import com.yen.ShoppingCart.repository.Categoryrepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    @Autowired
    private Categoryrepository categoryrepository;

    // Approach 3+5: cached read; routes to replica when replica is enabled
    @Transactional(readOnly = true)
    @Cacheable(value = RedisConfig.CACHE_CATEGORIES, key = "'all'")
    public List<Category> listCategories() {

        return categoryrepository.findAll();
    }

    // Approach 3: evict cache on create/update so next read is fresh
    @CacheEvict(value = RedisConfig.CACHE_CATEGORIES, allEntries = true)
    public void createCategory(Category category) {
        categoryrepository.save(category);
    }

    @Transactional(readOnly = true)
    public Category readCategory(String categoryName) {
        return categoryrepository.findByCategoryName(categoryName);
    }

    @Transactional(readOnly = true)
    public Optional<Category> readCategory(Integer categoryId) {
        return categoryrepository.findById(categoryId);
    }

    @CacheEvict(value = RedisConfig.CACHE_CATEGORIES, allEntries = true)
    public void updateCategory(Integer categoryID, Category newCategory) {
        Category category = categoryrepository.findById(categoryID).get();
        category.setCategoryName(newCategory.getCategoryName());
        category.setDescription(newCategory.getDescription());
        category.setImageUrl(newCategory.getImageUrl());
        categoryrepository.save(category);
    }

}
