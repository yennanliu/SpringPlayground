//package com.yen.ShoppingCart.repository;
//
//import com.yen.ShoppingCart.model.Cart;
//import com.yen.ShoppingCart.model.User;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
////@RunWith(MockitoJUnitRunner.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@DataJpaTest
//class CartRepositoryTest {
//
//    @Autowired
//    private CartRepository cartRepository;
//
//    @Test
//    public void testFindAllByUserOrderByCreatedDateDesc() {
//
//        List<Cart> carts = new ArrayList<>();
//        Cart cart1 = new Cart();
//        cart1.setId(1);
//        //cart1.setUser(user);
//        //cart1.setCreatedDate(now().minusDays(1));
//
//        Cart cart2 = new Cart();
//        cart2.setId(2);
//        //cart2.setUser(user);
//        //cart2.setCreatedDate(LocalDateTime.now());
//
//        carts.add(cart1);
//        carts.add(cart2);
//
//        cartRepository.save(cart1);
//        cartRepository.save(cart2);
//
//        //Mockito.when(cartRepository.findAll()).thenReturn(carts);
//
//        List<Cart> result = cartRepository.findAll();
//        System.out.println(">>> result = " + result);
//
//        Assert.assertEquals(2, result.size());
//        Assert.assertEquals(cart2.getId(), result.get(0).getId());
//        Assert.assertEquals(cart1.getId(), result.get(1).getId());
//    }
//
//    @Test
//    public void testDeleteByUser() {
//        List<Cart> carts = new ArrayList<>();
//        Cart cart1 = new Cart();
//        cart1.setId(1);
//        //cart1.setUser(user);
//        //cart1.setCreatedDate(LocalDateTime.now().minusDays(1));
//
//        Cart cart2 = new Cart();
//        cart2.setId(2);
//        //cart2.setUser(user);
//        //cart2.setCreatedDate(LocalDateTime.now());
//
//        carts.add(cart1);
//        carts.add(cart2);
//
//        //Mockito.when(cartRepository.deleteByUser(user)).thenReturn(carts);
//
//        //List<Cart> result = cartRepository.deleteByUser(user);
//
////        Assert.assertEquals(2, result.size());
////        Assert.assertEquals(cart1.getId(), result.get(0).getId());
////        Assert.assertEquals(cart2.getId(), result.get(1).getId());
//    }
//
//}