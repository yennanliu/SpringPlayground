package com.yen.ShoppingCart.service;

import com.yen.ShoppingCart.model.Category;
import com.yen.ShoppingCart.model.Product;
import com.yen.ShoppingCart.model.dto.product.ProductDto;
import com.yen.ShoppingCart.repository.ProductRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    Product product;

    ProductDto productDto;

    Category category;

    List<Product> productList;

    @BeforeEach
    public void setUp(){

        System.out.println("setup ...");
        Category category = new Category();
        product = new Product("prod_name","img_url", 100.0, "some desp", category);
        product.setId(1);
        productDto = new ProductDto(product);

        productList = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            product.setId(i+2);
            productList.add(product);
        }
    }

    @Test
    public void testAddProductSuccess(){

        // TODO : adjust test logic
        Mockito.when(productRepository.save(product)).thenReturn(product);
        productService.addProduct(productDto, category);
    }

    @Test
    public void testGetProductFromDto(){

        Product receivedProd = ProductService.getProductFromDto(productDto, category);
        assertEquals(receivedProd.getName(), "prod_name");
        assertEquals(receivedProd.getImageURL(), "img_url");
        assertEquals(receivedProd.getPrice(), 100.0);
        assertEquals(receivedProd.getDescription(), "some desp");
        assertEquals(receivedProd.getCategory(), null);
    }

    @Test
    public void ShouldReturnProductList(){

        Mockito.when(productRepository.findAll()).thenReturn(productList);
        List<ProductDto> res = productService.listProducts();
        assertEquals(res.size(), 3);
        assertEquals(res.get(0).getPrice(), 100.0);
        assertEquals(res.get(0).getName(), "prod_name");
        assertEquals(res.get(0).getImageURL(), "img_url");
        assertEquals(res.get(0).getDescription(), "some desp");
    }

    @Test
    public void ShouldReturnNullIfNullProductList(){

        Mockito.when(productRepository.findAll()).thenReturn(new ArrayList<>());
        List<ProductDto> res = productService.listProducts();
        assertEquals(res.size(), 0);
    }

    @Test
    public void testGetDtoFromProduct(){

        ProductDto receivedProdDTO = ProductService.getDtoFromProduct(product);
        assertEquals(receivedProdDTO.getName(), "prod_name");
        assertEquals(receivedProdDTO.getImageURL(), "img_url");
        assertEquals(receivedProdDTO.getDescription(), "some desp");
        assertEquals(receivedProdDTO.getPrice(), 100.0);
    }

    @Test
    public void testUpdateProduct(){

        // TODO : fix below test logic
        Product newProduct = new Product("new_prod_name","new_img_url", 999.0, "new desp", category);
        newProduct.setId(1);
        ProductDto newProductDto = new ProductDto();
        Mockito.when(productRepository.save(product)).thenReturn(newProduct);
        productService.updateProduct(1, newProductDto, category);
        assertEquals(newProduct.getName(), "new_prod_name");
    }

}