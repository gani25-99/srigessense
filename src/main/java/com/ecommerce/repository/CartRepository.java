package com.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByUser(User user);

    Optional<Cart> findByUserAndProduct(User user, Product product);

    boolean existsByUserAndProduct(User user, Product product);

    void deleteByUserAndProduct(User user, Product product);

}