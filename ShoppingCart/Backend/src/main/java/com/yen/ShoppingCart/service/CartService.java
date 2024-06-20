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
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public CartService(){

    }

    public CartService(CartRepository cartRepository) {

        this.cartRepository = cartRepository;
    }

    public void addToCart(AddToCartDto addToCartDto, Product product, User user){

        Cart cart = new Cart(product, addToCartDto.getQuantity(), user);
        cartRepository.save(cart);
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
