package com.yen.ShoppingCart.service;

import com.yen.ShoppingCart.model.User;
import com.yen.ShoppingCart.model.WishList;
import com.yen.ShoppingCart.repository.WishListRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class WishListServiceTest {

    @Mock
    private WishListRepository wishListRepository;

    @InjectMocks
    private WishListService wishListService;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateWishList(){

        WishList wishList = new WishList();
        // Set any necessary fields in wishList here

        wishListService.createWishlist(wishList);

        verify(wishListRepository, times(1)).save(wishList);
    }

    @Test
    public void testReadWishList() {
        // Given
        User user = new User();
        user.setId(1);

        WishList item1 = new WishList();
        item1.setId(1);
        item1.setUser(user);

        WishList item2 = new WishList();
        item2.setId(2);
        item2.setUser(user);

        List<WishList> itemList = Arrays.asList(item1, item2);

        // mock
        when(wishListRepository.findAll()).thenReturn(itemList);

        // When
        List<WishList> result = wishListService.readWishList(1);

        // Then
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
    }

}
