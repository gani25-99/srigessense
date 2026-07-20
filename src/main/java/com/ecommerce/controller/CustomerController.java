package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.service.ProductService;

@Controller
public class CustomerController {

    @Autowired
    private ProductService productService;

    @GetMapping("/home")
    public String home(Model model) {

        model.addAttribute("products", productService.getAllProducts());

        return "home";
    }

    @GetMapping("/search")
    public String search(@RequestParam String keyword, Model model) {

        model.addAttribute("products",
                productService.searchProducts(keyword));

        return "home";
    }

    @GetMapping("/category")
    public String category(@RequestParam Long id, Model model) {

        model.addAttribute("products",
                productService.getProductsByCategory(id));

        return "home";
    }

}