package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecommerce.entity.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    @Query("SELECT SUM(o.totalAmount) FROM Orders o")
    Double getTotalRevenue();

}