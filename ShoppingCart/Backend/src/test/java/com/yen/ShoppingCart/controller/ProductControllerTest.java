package com.yen.ShoppingCart.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yen.ShoppingCart.common.ApiResponse;
import com.yen.ShoppingCart.model.Category;
import com.yen.ShoppingCart.model.Product;
import com.yen.ShoppingCart.model.dto.product.ProductDto;
import com.yen.ShoppingCart.repository.ProductRepository;
import com.yen.ShoppingCart.service.CategoryService;
import com.yen.ShoppingCart.service.ProductService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    ProductService productService;

    @MockBean
    CategoryService categoryService;

    @Mock
    ProductRepository productRepository;

    Product product;

    ProductDto productDto;

    Category category;

    Category category_2;

    List<Product> productList;

    List<ProductDto> productDtoList;

    @BeforeEach
    public void before(){

        System.out.println("before ...");

        Category category = new Category();

        category_2 = new Category("new_name", "new_desp");
        category_2.setId(1);

        product = new Product("prod_name","img_url", 100.0, "some desp", category);
        product.setId(1);
        productDto = new ProductDto(product);

        productList = new ArrayList<>();
        productDtoList = new ArrayList<>();

        for (int i = 0; i < 3; i++){

            Product tmpProd = new Product("prod_name","img_url", 100.0, "some desp", category);
            tmpProd.setId(i+2);
            productList.add(tmpProd);

            productDto = new ProductDto(product);
            productDtoList.add(productDto);
        }
    }

    @Test
    public void shouldGetNotNullProducts() throws Exception {

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


    @Test
    public void shouldGetNullProducts() throws Exception {

        // mock
        when(productService.listProducts()).
                thenReturn(new ArrayList<>());
        // test
        String expect = "[]";
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/product/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expect));
    }

    @Test
    public void shouldAddProduct() throws Exception {

        // mock
        //given(productService.addProduct(productDto, category)).willAnswer((invocation -> invocation.getArgument(0)));
        //given(categoryService.readCategory(anyInt())).willAnswer((invocation -> invocation.getArgument(0)));
        when(categoryService.readCategory(anyInt()))
                .thenReturn(Optional.ofNullable(category_2));

        ResponseEntity resp = new ResponseEntity<>(new ApiResponse(true, "Product has been added"), HttpStatus.CREATED);
        ResultActions response = mockMvc.perform(post("/product/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resp))
        );

        System.out.println("response = " + response);
        // test
        //response.andExpect(MockMvcResultMatchers.status().isOk());
    }

}