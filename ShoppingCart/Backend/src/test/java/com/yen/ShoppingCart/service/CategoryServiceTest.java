package com.yen.ShoppingCart.service;

import com.yen.ShoppingCart.model.Category;
import com.yen.ShoppingCart.repository.Categoryrepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CategoryServiceTest {

    @Mock
    Categoryrepository categoryrepository;

    @InjectMocks
    CategoryService categoryService;

    List<Category> categoryList;

    Category category_1;

    Category new_category;

    @BeforeEach
    public void setUp(){

        System.out.println("setup ...");

        category_1 = new Category();
        category_1.setId(1);

        new_category = new Category("new_name", "new_desp");
        new_category.setId(1);

        categoryList = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            categoryList.add(new Category("name-"+i, "desp"));
        }

    }

    @Test
    public void shouldListCategoriesIfNotNull(){

        // mock
        Mockito.when(categoryrepository.findAll()).thenReturn(categoryList);
        assertEquals(categoryService.listCategories(), categoryList);
        assertEquals(categoryService.listCategories().size(), 3);
    }

    @Test
    public void shouldListNullArrayIfNotNull(){

        // mock
        Mockito.when(categoryrepository.findAll()).thenReturn(new ArrayList<>());
        assertEquals(categoryService.listCategories(), new ArrayList<>());
        assertEquals(categoryService.listCategories().size(), 0);
    }

    @Test
    public void testUpdateCategory(){

        // mock
        Mockito.when(categoryrepository.findById(1)).thenReturn(Optional.ofNullable(category_1));
        Mockito.when(categoryrepository.save(new_category)).thenReturn(new_category);

        categoryService.updateCategory(1, new_category);
        assertEquals(category_1.getCategoryName(), "new_name");
        assertEquals(category_1.getDescription(), "new_desp");
    }

}