//package com.yen.springWarehouse.controller;
//
//import com.yen.springWarehouse.bean.ProductType;
//import com.yen.springWarehouse.service.ProductTypeService;
//import org.junit.runner.RunWith;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@WebMvcTest(ProductTypeController.class)
//class ProductTypeControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ProductTypeService productTypeService;
//
//    @Test
//    public void testInputType() throws Exception {
//
//        // mock service
//        when(productTypeService.save(new ProductType(123, "some_type_name"))).
//                thenReturn(true);
//
//        // controller call
////        mockMvc.perform(get("/productType/create"))
////                .andExpect(status().isOk());
//    }
//
//}