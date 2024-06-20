package com.yen.ShoppingCart.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import com.yen.ShoppingCart.common.ApiResponse;
import com.yen.ShoppingCart.enums.Role;
import com.yen.ShoppingCart.model.Category;
import com.yen.ShoppingCart.model.Product;
import com.yen.ShoppingCart.model.User;
import com.yen.ShoppingCart.model.WishList;
import com.yen.ShoppingCart.service.AuthenticationService;
import com.yen.ShoppingCart.service.ProductService;
import com.yen.ShoppingCart.service.WishListService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class WishListControllerTest {

    @Mock
    private WishListService wishListService;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private WishListController wishListController;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }


//    @Test
//    public void testGetWishList() {
//
//        // Given
//        User user = new User("John", "Doe", "john.doe@example.com", Role.USER, "encryptedPassword");
//
//        Category category = new Category();
//        Product product1 = new Product("prod_name","img_url", 100.0, "some desp", category);
//        Product product2 = new Product("prod_name","img_url", 100.0, "some desp", category);
//        List<WishList> wishList = new ArrayList<>();
//        wishList.add(new WishList(user, product1));
//        wishList.add(new WishList(user, product2));
//
//        when(authenticationService.getUser(anyString())).thenReturn(user);
//        when(wishListService.readWishList(anyInt())).thenReturn(wishList);
//        //when(productService.getDtoFromProduct(any())).thenCallRealMethod();
//
//        // When
//        ResponseEntity<List<ProductDto>> response = wishListController.getWishList("token");
//
//        // Then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(2, response.getBody().size());
//    }

    @Test
    public void testAddWishList() {

        // Given
        User user = new User("John", "Doe", "john.doe@example.com", Role.USER, "encryptedPassword");

        Category category = new Category();
        Product product1 = new Product("prod_name","img_url", 100.0, "some desp", category);
        Product product2 = new Product("prod_name","img_url", 100.0, "some desp", category);
        List<WishList> wishList = new ArrayList<>();
        wishList.add(new WishList(user, product1));
        wishList.add(new WishList(user, product2));

        // mock
        when(authenticationService.getUser(anyString())).thenReturn(user);
        //when(wishListService.createWishlist(any())).thenReturn(wishList);

        // When
        ResponseEntity<ApiResponse> response = wishListController.addWishList(product1, "token");

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals("Add to wishlist", response.getBody().getMessage());
    }

}