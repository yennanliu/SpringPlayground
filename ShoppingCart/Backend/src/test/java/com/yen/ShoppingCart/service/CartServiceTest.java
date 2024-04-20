package com.yen.ShoppingCart.service;

import com.yen.ShoppingCart.enums.Role;
import com.yen.ShoppingCart.model.*;
import com.yen.ShoppingCart.model.dto.cart.CartDto;
import com.yen.ShoppingCart.model.dto.cart.CartItemDto;
import com.yen.ShoppingCart.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.util.DateUtil.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CartServiceTest {

    @Mock
    CartRepository cartRepository;

    @InjectMocks
    CartService cartService;

    Product product;

    User user1;
    User user2;

    Cart cart;

    Category category;

    @BeforeEach
    public void setUp(){

        MockitoAnnotations.initMocks(this);

        user1 = new User("f_name", "l_name", "email", Role.USER, "pwd");
        user2 = new User();
        category = new Category("new_name", "new_desp");
        product = new Product("prod_name","img_url", 100.0, "some desp", category);
        product.setId(1);

        Cart cart = new Cart(product, 1, user1);
    }

    @Test
    public void shouldAddCart(){

        // addToCart
        // mock
        //Mockito.when(tokenRepository.findTokenByToken("my_token")).thenReturn(token1);
        when(cart).thenReturn(cart);
        System.out.println(123);
    }

    @Test
    public void testListCartItems() {

        // Given
        User user = new User("John", "Doe", "john.doe@example.com", Role.USER, "encryptedPassword");

        List<Cart> cartList = new ArrayList<>();
        Product prod1 = new Product("prod_name-1","img_url", 100.0, "some desp", category);
        Product prod2 = new Product("prod_name-2","img_url", 100.0, "some desp", category);

        cartList.add(new Cart(prod1, 2, user));
        cartList.add(new Cart(prod2, 1, user));

        when(cartRepository.findAllByUserOrderByCreatedDateDesc(any(User.class))).thenReturn(cartList);

        // When
        CartDto cartDto = cartService.listCartItems(user);

        // Then
        assertEquals(2, cartDto.getCartItems().size());
        assertEquals(300.0, cartDto.getTotalCost());
    }

    @Test
    public void testGetDtoFromCart() {

        // Given
        Category category1 = new Category(1, "name", "desp", "url");
        Cart cart = new Cart(new Product("Product1", "url", 10.0, "desp", category1), 2, new User());

        // When
        CartItemDto cartItemDto = CartService.getDtoFromCart(cart);

        // Then
        assertEquals(cart.getProduct().getName(), cartItemDto.getProduct().getName());
        assertEquals(cart.getQuantity(), cartItemDto.getQuantity());
    }


}