package com.yen.ShoppingCart.service;

import com.yen.ShoppingCart.model.WishList;
import com.yen.ShoppingCart.repository.WishListRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class WishListService {

    private final WishListRepository wishListRepository;

    public WishListService(WishListRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
    }

    public void createWishlist(WishList wishList) {

        wishListRepository.save(wishList);
    }

    public List<WishList> readWishList(Integer userId) {

        //return wishListRepository.findAllByUserIdOrderByCreatedDateDesc(userId);
        //return wishListRepository.findAll();

        // get all item in wishlist with given userid
        List<WishList> items = wishListRepository.findAll()
                .stream().filter(x -> x.getUser().getId() == userId)
                .collect(Collectors.toList());

        return items;
    }

}