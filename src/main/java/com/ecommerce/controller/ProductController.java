package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ecommerce.service.ProductService;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("products", productService.getAllProducts());

        return "index";
    }

    @GetMapping("/admin/products")
    public String adminProducts() {
        return "admin-products";
    }

    @GetMapping("/success")
    public String success() {
        return "success";
    }
}