package com.yen.ShoppingCart.controller;

import com.yen.ShoppingCart.model.Category;
import com.yen.ShoppingCart.model.Product;
import com.yen.ShoppingCart.model.dto.product.ProductDto;
import com.yen.ShoppingCart.repository.ProductRepository;
import com.yen.ShoppingCart.service.CategoryService;
import com.yen.ShoppingCart.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @MockBean
    CategoryService categoryService;

    @Mock
    ProductRepository productRepository;

    Product product;

    ProductDto productDto;

    Category category;

    List<Product> productList;

    List<ProductDto> productDtoList;

    @BeforeEach
    public void before(){

        System.out.println("before ...");

        Category category = new Category();
        product = new Product("prod_name","img_url", 100.0, "some desp", category);
        product.setId(1);
        productDto = new ProductDto(product);

        productList = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            Product tmpProd = new Product("prod_name","img_url", 100.0, "some desp", category);
            tmpProd.setId(i+2);
            productList.add(tmpProd);
        }

        productDtoList = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            Product tmpProd = new Product("prod_name","img_url", 100.0, "some desp", category);
            productDto = new ProductDto(product);
            tmpProd.setId(i+2);
            productDtoList.add(productDto);
        }

    }

    @Test
    public void testGetProducts() throws Exception {

        // mock
        when(productService.listProducts()).
                thenReturn(productDtoList);
        // test
        String expect = "[{\"id\":1,\"name\":\"prod_name\",\"imageURL\":\"img_url\",\"price\":100.0,\"description\":\"some desp\",\"categoryId\":null},{\"id\":1,\"name\":\"prod_name\",\"imageURL\":\"img_url\",\"price\":100.0,\"description\":\"some desp\",\"categoryId\":null},{\"id\":1,\"name\":\"prod_name\",\"imageURL\":\"img_url\",\"price\":100.0,\"description\":\"some desp\",\"categoryId\":null}]";
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/product/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expect));
    }


}