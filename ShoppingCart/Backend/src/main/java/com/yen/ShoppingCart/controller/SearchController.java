package com.yen.ShoppingCart.controller;

import com.yen.ShoppingCart.model.Product;
import com.yen.ShoppingCart.model.dto.product.ProductDto;
import com.yen.ShoppingCart.service.CategoryService;
import com.yen.ShoppingCart.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    ProductService productService;

    @GetMapping("/api")
    public ResponseEntity<List<ProductDto>> searchProducts(@RequestParam("query") String query) {
        List<Product> products = productService.searchProducts(query);
        return new ResponseEntity<List<ProductDto>>(getProductDtoFromProduct(products), HttpStatus.OK);
    }

    private List<ProductDto> getProductDtoFromProduct(List<Product> products){
        List<ProductDto> body = new ArrayList<>();
        for (Product product : products){
            ProductDto dto = new ProductDto();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setImageURL(product.getImageURL());
            dto.setPrice(product.getPrice());
            dto.setCategoryId(product.getCategory().getId());
            body.add(dto);
        }
        return body;
    }

}
