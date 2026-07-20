package com.ecommerce.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ecommerce.entity.Orders;
import com.ecommerce.entity.User;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.CartService;
import com.ecommerce.service.EmailService;
import com.ecommerce.service.InvoiceService;
import com.ecommerce.service.OrderService;
import com.lowagie.text.DocumentException;

import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/checkout")
    public String checkout(Model model) {
        model.addAttribute("total", cartService.getGrandTotal());
        model.addAttribute("order", new Orders());
        return "checkout";
    }

    @PostMapping("/place-order")
    public String placeOrder(@ModelAttribute Orders order,
                             HttpSession session)
            throws DocumentException, IOException {

        order.setTotalAmount(cartService.getGrandTotal());

        String mobile = (String) session.getAttribute("mobile");

        if (mobile == null) {
            return "redirect:/";
        }

        Optional<User> optional = userRepository.findByMobile(mobile);

        if (optional.isEmpty()) {
            return "redirect:/";
        }

        User user = optional.get();

        if (order.getEmail() != null && !order.getEmail().isBlank()) {
            user.setEmail(order.getEmail());
            userRepository.save(user);
        }

        if ("COD".equals(order.getPaymentMethod())) {
            order.setPaymentStatus("PENDING");
            order.setTransactionId(null);
        } else if ("ONLINE".equals(order.getPaymentMethod())) {
            order.setPaymentStatus("PAID");
            order.setTransactionId("TXN" + System.currentTimeMillis());
        } else {
            return "redirect:/checkout";
        }

        Orders savedOrder = orderService.placeOrder(order);

        byte[] invoice = invoiceService.generateInvoice(savedOrder.getId());

        emailService.sendEmail(
                user.getEmail(),
                "Order Confirmed - SRIG",
                "Hello " + user.getName()
                        + ",\n\nYour order has been placed successfully."
                        + "\nInvoice No : INV-" + savedOrder.getId()
                        + "\nPayment Method : " + savedOrder.getPaymentMethod()
                        + "\nTotal Amount : ₹" + savedOrder.getTotalAmount()
                        + "\n\nPlease find your invoice attached."
                        + "\n\nThank you for shopping with SRIG.",
                invoice
        );

        return "redirect:/success";
    }

    @GetMapping("/my-orders")
    public String myOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "my-orders";
    }

    @GetMapping("/track-order/{id}")
    public String trackOrder(@PathVariable Long id, Model model) {
        Orders order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "track-order";
    }
}
