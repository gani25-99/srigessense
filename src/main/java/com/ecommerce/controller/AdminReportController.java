package com.ecommerce.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ecommerce.entity.Orders;
import com.ecommerce.entity.Product;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.ProductService;

@Controller
public class AdminReportController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/admin/reports")
    public String reports(Model model) {

        // Dashboard Counts

        long totalProducts = productService.countProducts();

        long totalOrders = orderService.countOrders();

        long totalUsers = userRepository.count();

        // Revenue

        BigDecimal revenue = orderService.getTotalRevenue();

        // Sales

        
        List<Product> lowStockProducts =
                productService.getLowStockProducts();

        // Orders

        List<Orders> recentOrders =
                orderService.getRecentOrders();

        // Model

        model.addAttribute("products", totalProducts);

        model.addAttribute("orders", totalOrders);

        model.addAttribute("users", totalUsers);

        model.addAttribute("revenue", revenue);

        
        model.addAttribute("lowStockProducts",
                lowStockProducts);

        model.addAttribute("recentOrders",
                recentOrders);

        return "admin-reports";
    }

}