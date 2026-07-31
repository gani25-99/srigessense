package com.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
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