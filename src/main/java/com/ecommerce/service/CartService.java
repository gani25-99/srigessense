package com.ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.repository.CartRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    // ==========================
    // GET USER CART
    // ==========================

    public List<Cart> getCart(User user) {

        return cartRepository.findByUser(user);

    }

    // ==========================
    // ADD TO CART
    // ==========================

   public void add(User user, Product product) {

    Optional<Cart> existing =
            cartRepository.findByUserAndProduct(user, product);

    if (existing.isPresent()) {

        Cart cart = existing.get();

        if (cart.getQuantity() < product.getQuantity()) {

            cart.setQuantity(cart.getQuantity() + 1);

            cartRepository.save(cart);

        }

        return;
    }

    if (product.getQuantity() > 0) {

        Cart cart = new Cart();

        cart.setUser(user);

        cart.setProduct(product);

        cart.setQuantity(1);

        cartRepository.save(cart);

    }

}
    // ==========================
    // INCREASE QUANTITY
    // ==========================

   public void increase(User user, Product product) {

    Optional<Cart> existing =
            cartRepository.findByUserAndProduct(user, product);

    if (existing.isPresent()) {

        Cart cart = existing.get();

        if (cart.getQuantity() < product.getQuantity()) {

            cart.setQuantity(cart.getQuantity() + 1);

            cartRepository.save(cart);

        }

    }

}

    // ==========================
    // DECREASE QUANTITY
    // ==========================

    public void decrease(User user,
                         Product product) {

        Optional<Cart> existing =
                cartRepository.findByUserAndProduct(user, product);

        if (existing.isPresent()) {

            Cart cart = existing.get();

            if (cart.getQuantity() > 1) {

                cart.setQuantity(cart.getQuantity() - 1);

                cartRepository.save(cart);

            } else {

                cartRepository.delete(cart);

            }

        }

    }

    // ==========================
    // REMOVE
    // ==========================

    public void remove(User user,
                       Product product) {

        Optional<Cart> existing =
                cartRepository.findByUserAndProduct(user, product);

        existing.ifPresent(cartRepository::delete);

    }

}