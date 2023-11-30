package com.yen.ShoppingCart.controller;

import com.yen.ShoppingCart.common.ApiResponse;
import com.yen.ShoppingCart.model.Product;
import com.yen.ShoppingCart.model.User;
import com.yen.ShoppingCart.model.WishList;
import com.yen.ShoppingCart.service.AuthenticationService;
import com.yen.ShoppingCart.service.WishListService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class WishListController {

    @Autowired
    private WishListService wishListService;

    @Autowired
    private AuthenticationService authenticationService;


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addWishList(@RequestBody Product product, @RequestParam("token") String token) {

        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);
        WishList wishList = new WishList(user, product);
        wishListService.createWishlist(wishList);
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Add to wishlist"), HttpStatus.CREATED);
    }

}
