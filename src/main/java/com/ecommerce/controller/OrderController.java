package com.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.Orders;
import com.ecommerce.entity.User;
import com.ecommerce.repository.OrderItemRepository;
import com.ecommerce.repository.OrdersRepository;
import com.ecommerce.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;


    // =========================================================
    // MY ORDERS
    // =========================================================

    @GetMapping("/my-orders")
    public String myOrders(
            HttpSession session,
            Model model) {

        // =====================================================
        // LOGIN CHECK
        // =====================================================

        Optional<User> optionalUser =
                getLoggedInUser(session);

        if (optionalUser.isEmpty()) {

            return "redirect:/login";
        }

        User user =
                optionalUser.get();


        // =====================================================
        // GET ONLY LOGGED-IN USER'S ORDERS
        //
        // IMPORTANT:
        // Search by User, NOT recipient email.
        //
        // This allows:
        //
        // Order #101 -> Self
        // Order #102 -> Mother
        // Order #103 -> Brother
        // Order #104 -> Friend
        // =====================================================

        List<Orders> orders =
                ordersRepository
                        .findByUserOrderByPlacedAtDesc(
                                user);


        model.addAttribute(
                "orders",
                orders);


        return "my-orders";
    }


    // =========================================================
    // ORDER DETAILS
    // =========================================================

    @GetMapping("/order/{id}")
    public String orderDetails(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        // =====================================================
        // LOGIN CHECK
        // =====================================================

        Optional<User> optionalUser =
                getLoggedInUser(session);

        if (optionalUser.isEmpty()) {

            return "redirect:/login";
        }

        User user =
                optionalUser.get();


        // =====================================================
        // FIND ORDER
        // =====================================================

        Optional<Orders> optionalOrder =
                ordersRepository.findById(id);

        if (optionalOrder.isEmpty()) {

            return "redirect:/my-orders";
        }

        Orders order =
                optionalOrder.get();


        // =====================================================
        // SECURITY CHECK
        //
        // Check the ACCOUNT OWNER.
        //
        // Do NOT compare order.email because that is now
        // the recipient's email.
        // =====================================================

        if (!isOrderOwnedByUser(order, user)) {

            return "redirect:/my-orders";
        }


        // =====================================================
        // GET ORDER ITEMS
        // =====================================================

        List<OrderItem> items =
                orderItemRepository.findByOrder(order);


        model.addAttribute(
                "order",
                order);

        model.addAttribute(
                "items",
                items);


        return "order-details";
    }


    // =========================================================
    // TRACK ORDER
    // =========================================================

    @GetMapping("/track-order/{id}")
    public String trackOrder(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        // =====================================================
        // LOGIN CHECK
        // =====================================================

        Optional<User> optionalUser =
                getLoggedInUser(session);

        if (optionalUser.isEmpty()) {

            return "redirect:/login";
        }

        User user =
                optionalUser.get();


        // =====================================================
        // FIND ORDER
        // =====================================================

        Optional<Orders> optionalOrder =
                ordersRepository.findById(id);

        if (optionalOrder.isEmpty()) {

            return "redirect:/my-orders";
        }

        Orders order =
                optionalOrder.get();


        // =====================================================
        // SECURITY CHECK
        // =====================================================

        if (!isOrderOwnedByUser(order, user)) {

            return "redirect:/my-orders";
        }


        model.addAttribute(
                "order",
                order);


        return "track-order";
    }


    // =========================================================
    // GET LOGGED-IN USER
    // =========================================================

    private Optional<User> getLoggedInUser(
            HttpSession session) {

        String mobile =
                (String) session.getAttribute("mobile");


        // =====================================================
        // NO SESSION
        // =====================================================

        if (mobile == null ||
                mobile.isBlank()) {

            return Optional.empty();
        }


        // =====================================================
        // FIND USER
        // =====================================================

        Optional<User> optionalUser =
                userRepository.findByMobile(mobile);


        // =====================================================
        // USER NO LONGER EXISTS
        // =====================================================

        if (optionalUser.isEmpty()) {

            session.invalidate();

            return Optional.empty();
        }


        return optionalUser;
    }


    // =========================================================
    // ORDER OWNERSHIP CHECK
    // =========================================================

    private boolean isOrderOwnedByUser(
            Orders order,
            User user) {

        if (order == null ||
                user == null) {

            return false;
        }


        // =====================================================
        // ORDER MUST HAVE AN ACCOUNT OWNER
        // =====================================================

        User orderUser =
                order.getUser();

        if (orderUser == null) {

            return false;
        }


        // =====================================================
        // COMPARE USER IDs
        //
        // This is the actual account ownership check.
        //
        // Recipient name/email/mobile are NOT used here.
        // =====================================================

        if (orderUser.getId() == null ||
                user.getId() == null) {

            return false;
        }


        return orderUser.getId()
                .equals(user.getId());
    }
}