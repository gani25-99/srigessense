package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.Product;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByProduct(Product product);

}