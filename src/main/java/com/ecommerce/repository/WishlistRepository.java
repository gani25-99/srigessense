package com.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.entity.Wishlist;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findByUser(User user);

    Optional<Wishlist> findByUserAndProduct(User user, Product product);

    boolean existsByUserAndProduct(User user, Product product);

    void deleteByUserAndProduct(User user, Product product);

}