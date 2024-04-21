package com.yen.ShoppingCart.controller;

import com.yen.ShoppingCart.common.ApiResponse;
import com.yen.ShoppingCart.enums.Role;
import com.yen.ShoppingCart.exception.AuthenticationFailException;
import com.yen.ShoppingCart.exception.CartItemNotExistException;
import com.yen.ShoppingCart.exception.ProductNotExistException;
import com.yen.ShoppingCart.model.Category;
import com.yen.ShoppingCart.model.Product;
import com.yen.ShoppingCart.model.User;
import com.yen.ShoppingCart.model.dto.cart.AddToCartDto;
import com.yen.ShoppingCart.model.dto.cart.CartDto;
import com.yen.ShoppingCart.model.dto.cart.CartItemDto;
import com.yen.ShoppingCart.service.AuthenticationService;
import com.yen.ShoppingCart.service.CartService;
import com.yen.ShoppingCart.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private ProductService productService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddToCart() throws AuthenticationFailException, ProductNotExistException {

        // Given
        AddToCartDto addToCartDto = new AddToCartDto(1,1,1);

        User user1 = new User("f_name", "l_name", "email", Role.USER, "pwd");
        Category category = new Category("new_name", "new_desp");
        Product product = new Product("prod_name","img_url", 100.0, "some desp", category);
        product.setId(1);

        String token = "token";

        // mock
        when(productService.getProductById(anyInt())).thenReturn(product);
        when(authenticationService.getUser(token)).thenReturn(user1);

        ResponseEntity<ApiResponse> response = cartController.addToCart(addToCartDto, token);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(cartService, times(1)).addToCart(addToCartDto, product, user1);
    }

    @Test
    public void testGetCartItems() throws AuthenticationFailException {

        // Given
        User user1 = new User("f_name", "l_name", "email", Role.USER, "pwd");
        Category category = new Category("new_name", "new_desp");
        Product product = new Product("prod_name","img_url", 100.0, "some desp", category);
        product.setId(1);

        List<CartItemDto> cartItems = new ArrayList<>();
        CartDto cartDto = new CartDto(cartItems, 0);

        String token = "token";

        // mock
        when(authenticationService.getUser(token)).thenReturn(user1);
        when(cartService.listCartItems(user1)).thenReturn(cartDto);

        ResponseEntity<CartDto> response = cartController.getCartItems(token);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cartDto, response.getBody());
    }

    @Test
    public void testUpdateCartItem() throws AuthenticationFailException, ProductNotExistException {

        // Given
        User user1 = new User("f_name", "l_name", "email", Role.USER, "pwd");
        Category category = new Category("new_name", "new_desp");
        Product product = new Product("prod_name","img_url", 100.0, "some desp", category);
        product.setId(1);
        String token = "token";

        AddToCartDto addToCartDto = new AddToCartDto(1,1,1);
        
        when(productService.getProductById(anyInt())).thenReturn(product);
        when(authenticationService.getUser(token)).thenReturn(user1);

        // When
        ResponseEntity<ApiResponse> response = cartController.updateCartItem(addToCartDto, token);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cartService, times(1)).updateCartItem(addToCartDto, user1, product);
    }

    @Test
    public void testDeleteCartItem() throws AuthenticationFailException, CartItemNotExistException {

        // Given
        int itemId = 1;
        String token = "token";
        User user1 = new User("f_name", "l_name", "email", Role.USER, "pwd");
        user1.setId(1);
        Category category = new Category("new_name", "new_desp");
        Product product = new Product("prod_name","img_url", 100.0, "some desp", category);
        product.setId(1);

        when(authenticationService.getUser(token)).thenReturn(user1);

        // When
        ResponseEntity<ApiResponse> response = cartController.deleteCartItem(itemId, token);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cartService, times(1)).deleteCartItem(itemId, user1.getId());
    }

}
