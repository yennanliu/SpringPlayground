package com.yen.ShoppingCart.service;

import com.yen.ShoppingCart.enums.Role;
import com.yen.ShoppingCart.model.*;
import com.yen.ShoppingCart.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.util.DateUtil.now;
import static org.junit.jupiter.api.Assertions.*;

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

        System.out.println("setup ...");

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
        Mockito.when(cart).thenReturn(cart);
        System.out.println(123);
    }

}