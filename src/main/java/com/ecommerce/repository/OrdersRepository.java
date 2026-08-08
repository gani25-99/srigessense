package com.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecommerce.entity.Orders;
import com.ecommerce.entity.User;

public interface OrdersRepository
        extends JpaRepository<Orders, Long> {


    // =========================================================
    // CUSTOMER ORDERS
    // =========================================================
    //
    // Finds orders belonging to the logged-in account.
    //
    // IMPORTANT:
    // Do NOT use recipient email here because an order can
    // be placed for another person.
    // =========================================================

    List<Orders> findByUserOrderByPlacedAtDesc(User user);


    // =========================================================
    // TOTAL REVENUE
    // =========================================================

    @Query("""
        SELECT COALESCE(SUM(o.totalAmount), 0)
        FROM Orders o
        """)
    Double getTotalRevenue();


    // =========================================================
    // TODAY SALES
    // =========================================================

    @Query("""
        SELECT COALESCE(SUM(o.totalAmount), 0)
        FROM Orders o
        WHERE DATE(o.placedAt) = :date
        """)
    Double getTodaySales(
            java.time.LocalDate date);


    // =========================================================
    // MONTHLY SALES
    // =========================================================

    @Query("""
        SELECT COALESCE(SUM(o.totalAmount), 0)
        FROM Orders o
        WHERE MONTH(o.placedAt) = :month
        AND YEAR(o.placedAt) = :year
        """)
    Double getMonthlySales(
            int month,
            int year);


    // =========================================================
    // YEARLY SALES
    // =========================================================

    @Query("""
        SELECT COALESCE(SUM(o.totalAmount), 0)
        FROM Orders o
        WHERE YEAR(o.placedAt) = :year
        """)
    Double getYearlySales(
            int year);


    // =========================================================
    // RECENT ORDERS
    // =========================================================

    List<Orders>
    findTop10ByOrderByPlacedAtDesc();


    // =========================================================
    // ALL ORDERS
    // =========================================================

    List<Orders>
    findAllByOrderByPlacedAtDesc();

}