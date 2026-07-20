package com.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.Product;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.ProductRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    // Add Product To Cart
    public void addToCart(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product Not Found"));

        // Out of stock
        if (product.getQuantity() <= 0) {
            throw new RuntimeException("Product is out of stock");
        }

        // Check if product already exists in cart
        Cart cart = cartRepository.findByProduct(product);

        if (cart != null) {

            // Don't exceed available stock
            if (cart.getQuantity() >= product.getQuantity()) {
                throw new RuntimeException("Maximum available stock reached");
            }

            cart.setQuantity(cart.getQuantity() + 1);

        } else {

            cart = new Cart();
            cart.setProduct(product);
            cart.setQuantity(1);
        }

        cartRepository.save(cart);
    }

    // Get Cart Items
    public List<Cart> getCartItems() {
        return cartRepository.findAll();
    }

    // Delete Cart Item
    public void remove(Long id) {
        cartRepository.deleteById(id);
    }

    // Increase Quantity
    public void increaseQuantity(Long id) {

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart Item Not Found"));

        Product product = cart.getProduct();

        if (cart.getQuantity() >= product.getQuantity()) {
            throw new RuntimeException("Maximum available stock reached");
        }

        cart.setQuantity(cart.getQuantity() + 1);

        cartRepository.save(cart);
    }

    // Decrease Quantity
    public void decreaseQuantity(Long id) {

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart Item Not Found"));

        if (cart.getQuantity() > 1) {

            cart.setQuantity(cart.getQuantity() - 1);

            cartRepository.save(cart);

        } else {

            cartRepository.delete(cart);

        }
    }

    // Calculate Total Price
    public Double getGrandTotal() {

        Double total = 0.0;

        List<Cart> cartItems = cartRepository.findAll();

        for (Cart item : cartItems) {

            total += item.getProduct().getPrice() * item.getQuantity();

        }

        return total;
    }

}