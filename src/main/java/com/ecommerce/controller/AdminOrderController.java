package com.ecommerce.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.Orders;
import com.ecommerce.repository.OrderItemRepository;
import com.ecommerce.repository.OrdersRepository;

@Controller
public class AdminOrderController {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    // ==========================
    // ALL ORDERS
    // ==========================

    @GetMapping("/admin/orders")
    public String orders(Model model) {

        List<Orders> orders =
                ordersRepository.findAllByOrderByPlacedAtDesc();

        model.addAttribute("orders", orders);

        return "admin-orders";

    }

    // ==========================
    // ORDER DETAILS
    // ==========================

    @GetMapping("/admin/orders/{id}")
    public String orderDetails(

            @PathVariable Long id,

            Model model) {

        Optional<Orders> optionalOrder =
                ordersRepository.findById(id);

        if (optionalOrder.isEmpty()) {

            return "redirect:/admin/orders";

        }

        Orders order =
                optionalOrder.get();

        List<OrderItem> items =
                orderItemRepository.findByOrder(order);

        model.addAttribute("order", order);

        model.addAttribute("items", items);

        return "admin-order-details";

    }

    // ==========================
    // EDIT ORDER
    // ==========================

    @GetMapping("/admin/orders/edit/{id}")
    public String editOrder(

            @PathVariable Long id,

            Model model) {

        Optional<Orders> optionalOrder =
                ordersRepository.findById(id);

        if (optionalOrder.isEmpty()) {

            return "redirect:/admin/orders";

        }

        Orders order =
                optionalOrder.get();

        List<OrderItem> items =
                orderItemRepository.findByOrder(order);

        model.addAttribute("order", order);

        model.addAttribute("items", items);

        return "admin-order-details";

    }
        // ==========================
    // UPDATE ORDER
    // ==========================

    @PostMapping("/admin/orders/update/{id}")
    public String updateOrder(

            @PathVariable Long id,

            @RequestParam String status,

            @RequestParam String paymentStatus,

            @RequestParam(required = false)
            String courierName,

            @RequestParam(required = false)
            String trackingNumber) {

        Optional<Orders> optionalOrder =
                ordersRepository.findById(id);

        if (optionalOrder.isPresent()) {

            Orders order =
                    optionalOrder.get();

            // ==========================
            // ORDER STATUS
            // ==========================

            order.setStatus(status);

            if ("Confirmed".equals(status)
                    && order.getConfirmedAt() == null) {

                order.setConfirmedAt(
                        LocalDateTime.now());

            }

            if ("Packed".equals(status)
                    && order.getPackedAt() == null) {

                order.setPackedAt(
                        LocalDateTime.now());

            }

            if ("Shipped".equals(status)
                    && order.getShippedAt() == null) {

                order.setShippedAt(
                        LocalDateTime.now());

            }

            if ("Out For Delivery".equals(status)
                    && order.getOutForDeliveryAt() == null) {

                order.setOutForDeliveryAt(
                        LocalDateTime.now());

            }

            if ("Delivered".equals(status)
                    && order.getDeliveredAt() == null) {

                order.setDeliveredAt(
                        LocalDateTime.now());

            }

            // ==========================
            // PAYMENT
            // ==========================

            order.setPaymentStatus(
                    paymentStatus);

            // ==========================
            // SHIPPING
            // ==========================

            order.setCourierName(
                    courierName);

            order.setTrackingNumber(
                    trackingNumber);

            ordersRepository.save(order);

        }

        return "redirect:/admin/orders/" + id;

    }

}