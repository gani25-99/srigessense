package com.ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.entity.Wishlist;
import com.ecommerce.repository.WishlistRepository;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    public List<Wishlist> getWishlist(User user) {

        return wishlistRepository.findByUser(user);

    }

    public void add(User user, Product product) {

        if (wishlistRepository.existsByUserAndProduct(user, product)) {
            return;
        }

        Wishlist wishlist = new Wishlist();

        wishlist.setUser(user);
        wishlist.setProduct(product);

        wishlistRepository.save(wishlist);

    }

    public void remove(User user, Product product) {

        Optional<Wishlist> wishlist =
                wishlistRepository.findByUserAndProduct(user, product);

        wishlist.ifPresent(wishlistRepository::delete);

    }

}