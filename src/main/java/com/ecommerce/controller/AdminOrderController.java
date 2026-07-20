package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.entity.Orders;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.service.OrderService;

@Controller
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/admin/orders")
    public String orders(Model model) {

        model.addAttribute("orders", orderService.getAllOrders());

        return "admin-orders";
    }

    @PostMapping("/admin/orders/update")
    public String updateStatus(
            @RequestParam Long id,
            @RequestParam String status,
            @RequestParam(required = false) String courierName,
            @RequestParam(required = false) String trackingNumber) {

        // Update Order Status
        orderService.updateStatus(id, status);

        // Save Courier Details
        orderService.updateCourierDetails(id, courierName, trackingNumber);

        return "redirect:/admin/orders";
    }

    @GetMapping("/admin/payment/{id}")
    public String markPayment(@PathVariable Long id) {

        Orders order = orderRepository.findById(id).orElseThrow();

        order.setPaymentStatus("PAID");

        orderRepository.save(order);

        return "redirect:/admin/orders";
    }

}