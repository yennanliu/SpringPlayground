package com.yen.ShoppingCart.service;

import com.yen.ShoppingCart.exception.CartItemNotExistException;
import com.yen.ShoppingCart.model.Cart;
import com.yen.ShoppingCart.model.Product;
import com.yen.ShoppingCart.model.User;
import com.yen.ShoppingCart.model.dto.cart.AddToCartDto;
import com.yen.ShoppingCart.model.dto.cart.CartDto;
import com.yen.ShoppingCart.model.dto.cart.CartItemDto;
import com.yen.ShoppingCart.repository.CartRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import jakarta.transaction.Transactional;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private RedissonClient redissonClient;

    public CartService(){

    }

    public CartService(CartRepository cartRepository) {

        this.cartRepository = cartRepository;
    }

    // Lock per user so concurrent add-to-cart for the same user is serialized.
    // Different users proceed in parallel.
    public void addToCart(AddToCartDto addToCartDto, Product product, User user){

        RLock lock = redissonClient.getLock("cart:user:" + user.getId());
        try {
            lock.lock(10, TimeUnit.SECONDS);
            Cart cart = new Cart(product, addToCartDto.getQuantity(), user);
            cartRepository.save(cart);
        } finally {
            if (lock.isHeldByCurrentThread()) lock.unlock();
        }
    }

    public CartDto listCartItems(User user) {

        List<Cart> cartList = cartRepository.findAllByUserOrderByCreatedDateDesc(user);
        List<CartItemDto> cartItems = new ArrayList<>();
        double totalCost = 0;

        // TODO : change to functional style
        for (Cart cart:cartList){
            CartItemDto cartItemDto = getDtoFromCart(cart);
            cartItems.add(cartItemDto);
            totalCost += (cartItemDto.getProduct().getPrice() * cartItemDto.getQuantity());
        }
        return new CartDto(cartItems, totalCost);
    }

    // local method
    public static CartItemDto getDtoFromCart(Cart cart) {

        return new CartItemDto(cart);
    }

    public void updateCartItem(AddToCartDto cartDto, User user,Product product){

        Cart cart = cartRepository.getOne(cartDto.getId());
        cart.setQuantity(cartDto.getQuantity());
        cart.setCreatedDate(new Date());
        cartRepository.save(cart);
    }

    public void deleteCartItem(int id,int userId) throws CartItemNotExistException {

        if (!cartRepository.existsById(id)){
            throw new CartItemNotExistException("Cart id is invalid, id = " + id + ", userId = " + userId);
        }
        cartRepository.deleteById(id);
    }

    public void deleteCartItems(int userId) {

        cartRepository.deleteAll();
    }


    public void deleteUserCartItems(User user) {

        cartRepository.deleteByUser(user);
    }

}
