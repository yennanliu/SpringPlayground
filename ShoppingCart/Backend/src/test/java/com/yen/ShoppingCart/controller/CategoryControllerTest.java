package com.yen.ShoppingCart.controller;

import com.yen.ShoppingCart.common.ApiResponse;
import com.yen.ShoppingCart.model.Category;
import com.yen.ShoppingCart.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCategories() {

        // Given
        List<Category> categories = new ArrayList<>();
        Category category1 = new Category("name1", "Category 1");
        Category category2 = new Category("name2", "Category 2");
        categories.add(category1);
        categories.add(category2);

        when(categoryService.listCategories()).thenReturn(categories);

        // When
        ResponseEntity<List<Category>> response = categoryController.getCategories();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categories, response.getBody());
    }

    @Test
    public void testCreateCategory() {

        // Given
        Category category = new Category("name1", "Category 1");

        when(categoryService.readCategory(category.getCategoryName())).thenReturn(null);

        // When
        ResponseEntity<ApiResponse> response = categoryController.createCategory(category);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(categoryService, times(1)).createCategory(category);
    }

    @Test
    public void testUpdateCategory() {

        // Given
        int categoryId = 1;
        Category category = new Category("name1", "Category 1");

        when(categoryService.readCategory(categoryId)).thenReturn(Optional.of(category));

        // When
        ResponseEntity<ApiResponse> response = categoryController.updateCategory(categoryId, category);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(categoryService, times(1)).updateCategory(categoryId, category);
    }

    @Test
    public void testUpdateCategoryCategoryNotFound() {

        // Given
        int categoryId = 1;
        Category category = new Category("name1", "Category 1");

        when(categoryService.readCategory(categoryId)).thenReturn(null);

        // When
        ResponseEntity<ApiResponse> response = categoryController.updateCategory(categoryId, category);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(categoryService, never()).updateCategory(categoryId, category);
    }

}
