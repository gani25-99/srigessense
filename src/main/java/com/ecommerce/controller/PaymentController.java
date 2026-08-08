package com.ecommerce.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecommerce.entity.Orders;
import com.ecommerce.entity.User;
import com.ecommerce.repository.OrdersRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.CheckoutService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CheckoutService checkoutService;


    // =========================================================
    // PAYMENT PAGE
    // =========================================================

    @GetMapping("/{orderId}")
    public String paymentPage(
            @PathVariable Long orderId,
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
                ordersRepository.findById(orderId);

        if (optionalOrder.isEmpty()) {

            return "redirect:/my-orders";
        }

        Orders order =
                optionalOrder.get();


        // =====================================================
        // ORDER OWNERSHIP CHECK
        // =====================================================

        if (!isOrderOwnedByUser(order, user)) {

            return "redirect:/my-orders";
        }


        // =====================================================
        // PAYMENT ALREADY COMPLETED
        // =====================================================

        if ("PAID".equalsIgnoreCase(
                order.getPaymentStatus())) {

            return "redirect:/order/"
                    + order.getId();
        }


        model.addAttribute(
                "order",
                order);


        return "payment";
    }


    // =========================================================
    // PAYMENT SUCCESS
    // =========================================================

    @PostMapping("/success/{orderId}")
    public String paymentSuccess(
            @PathVariable Long orderId,
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
                ordersRepository.findById(orderId);

        if (optionalOrder.isEmpty()) {

            return "redirect:/my-orders";
        }

        Orders order =
                optionalOrder.get();


        // =====================================================
        // OWNERSHIP CHECK
        // =====================================================

        if (!isOrderOwnedByUser(order, user)) {

            return "redirect:/my-orders";
        }


        // =====================================================
        // PREVENT DUPLICATE PAYMENT PROCESSING
        // =====================================================

        if ("PAID".equalsIgnoreCase(
                order.getPaymentStatus())) {

            model.addAttribute(
                    "order",
                    order);

            return "payment-success";
        }


        // =====================================================
        // PAYMENT DETAILS
        // =====================================================

        order.setPaymentMethod(
                "ONLINE");

        order.setPaymentStatus(
                "PAID");

        order.setTransactionId(
                "TXN-"
                        + UUID.randomUUID()
                                .toString()
                                .substring(
                                        0,
                                        10)
                                .toUpperCase());


        // =====================================================
        // ORDER STATUS
        // =====================================================

        order.setStatus(
                "Confirmed");

        order.setConfirmedAt(
                LocalDateTime.now());

        order.setExpectedDeliveryDate(
                LocalDate.now()
                        .plusDays(5));


        // =====================================================
        // SAVE PAYMENT
        // =====================================================

        Orders savedOrder =
                ordersRepository.save(order);


        // =====================================================
        // SEND INVOICE EMAIL
        //
        // CheckoutService:
        // 1. Generates branded PDF
        // 2. Attaches PDF
        // 3. Sends confirmation email
        // =====================================================

        checkoutService.sendInvoiceEmail(
                savedOrder);


        // =====================================================
        // SEND TO SUCCESS PAGE
        // =====================================================

        model.addAttribute(
                "order",
                savedOrder);


        return "payment-success";
    }


    // =========================================================
    // PAYMENT FAILED
    // =========================================================

    @GetMapping("/failed/{orderId}")
    public String paymentFailed(
            @PathVariable Long orderId,
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
                ordersRepository.findById(orderId);

        if (optionalOrder.isEmpty()) {

            return "redirect:/my-orders";
        }

        Orders order =
                optionalOrder.get();


        // =====================================================
        // OWNERSHIP CHECK
        // =====================================================

        if (!isOrderOwnedByUser(order, user)) {

            return "redirect:/my-orders";
        }


        // =====================================================
        // DO NOT CHANGE A PAID ORDER TO FAILED
        // =====================================================

        if (!"PAID".equalsIgnoreCase(
                order.getPaymentStatus())) {

            order.setPaymentMethod(
                    "ONLINE");

            order.setPaymentStatus(
                    "FAILED");

            ordersRepository.save(order);
        }


        model.addAttribute(
                "order",
                order);


        return "payment-failed";
    }


    // =========================================================
    // GET LOGGED-IN USER
    // =========================================================

    private Optional<User> getLoggedInUser(
            HttpSession session) {

        String mobile =
                (String) session.getAttribute(
                        "mobile");


        if (mobile == null ||
                mobile.isBlank()) {

            return Optional.empty();
        }


        Optional<User> optionalUser =
                userRepository.findByMobile(
                        mobile);


        if (optionalUser.isEmpty()) {

            session.invalidate();
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


        String orderEmail =
                order.getEmail();

        String userEmail =
                user.getEmail();


        if (orderEmail == null ||
                userEmail == null) {

            return false;
        }


        return orderEmail.equalsIgnoreCase(
                userEmail);
    }
}