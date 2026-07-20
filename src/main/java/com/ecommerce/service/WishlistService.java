package com.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.entity.Product;
import com.ecommerce.entity.Wishlist;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.WishlistRepository;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductRepository productRepository;

    public void add(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product Not Found"));

        Wishlist wishlist = new Wishlist();

        wishlist.setProduct(product);

        wishlistRepository.save(wishlist);

    }

    public List<Wishlist> getAll() {

        return wishlistRepository.findAll();

    }

    public void remove(Long id) {

        wishlistRepository.deleteById(id);

    }

}