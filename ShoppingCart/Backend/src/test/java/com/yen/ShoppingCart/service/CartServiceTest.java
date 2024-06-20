package com.yen.ShoppingCart.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.yen.ShoppingCart.enums.Role;
import com.yen.ShoppingCart.exception.CartItemNotExistException;
import com.yen.ShoppingCart.model.*;
import com.yen.ShoppingCart.model.dto.cart.AddToCartDto;
import com.yen.ShoppingCart.model.dto.cart.CartDto;
import com.yen.ShoppingCart.model.dto.cart.CartItemDto;
import com.yen.ShoppingCart.repository.CartRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

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
    public void addToCart() {

        AddToCartDto addToCartDto = new AddToCartDto();
        addToCartDto.setQuantity(1);
        Product product = new Product();
        User user = new User();

        cartService.addToCart(addToCartDto, product, user);

        verify(cartRepository, times(1)).save(any(Cart.class));
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

        // mock
        when(cartRepository.findAllByUserOrderByCreatedDateDesc(any(User.class))).thenReturn(cartList);

        // When
        CartDto cartDto = cartService.listCartItems(user);

        // Then
        assertEquals(2, cartDto.getCartItems().size());
        assertEquals(300.0, cartDto.getTotalCost());
    }

    @Test
    public void updateCartItem() {

        AddToCartDto addToCartDto = new AddToCartDto();
        addToCartDto.setId(1);
        addToCartDto.setQuantity(2);
        User user = new User();
        Product product = new Product();
        Cart cart = new Cart();

        // mock
        when(cartRepository.getOne(addToCartDto.getId())).thenReturn(cart);

        cartService.updateCartItem(addToCartDto, user, product);

        assertEquals(addToCartDto.getQuantity(), cart.getQuantity());
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    public void deleteCartItem() {
        int cartId = 1;
        int userId = 1;

        // mock
        when(cartRepository.existsById(cartId)).thenReturn(true);

        assertDoesNotThrow(() -> cartService.deleteCartItem(cartId, userId));

        verify(cartRepository, times(1)).deleteById(cartId);
    }

    @Test
    public void deleteCartItem_NotExist() {

        int cartId = 1;
        int userId = 1;

        // mock
        when(cartRepository.existsById(cartId)).thenReturn(false);

        assertThrows(CartItemNotExistException.class, () -> cartService.deleteCartItem(cartId, userId));
    }

    @Test
    public void deleteCartItems() {

        int userId = 1;

        cartService.deleteCartItems(userId);

        verify(cartRepository, times(1)).deleteAll();
    }

    @Test
    public void deleteUserCartItems() {
        User user = new User();

        cartService.deleteUserCartItems(user);

        verify(cartRepository, times(1)).deleteByUser(user);
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