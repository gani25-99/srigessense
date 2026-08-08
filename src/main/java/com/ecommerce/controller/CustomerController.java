package com.ecommerce.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.ecommerce.entity.User;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class CustomerController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // ==========================
    // HOME PAGE
    // ==========================

  // @GetMapping("/home")
public String home(HttpSession session, Model model) {

    System.out.println("========== CUSTOMER HOME ==========");
    System.out.println("Session ID     : " + session.getId());
    System.out.println("Session Mobile : " + session.getAttribute("mobile"));

    String mobile = (String) session.getAttribute("mobile");

    if (mobile == null) {

        System.out.println("Mobile is NULL");

        return "redirect:/login";
    }
//
    Optional<User> optionalUser =
            userRepository.findByMobile(mobile);

    System.out.println("User Found : " + optionalUser.isPresent());

    if (optionalUser.isEmpty()) {

        System.out.println("User NOT FOUND");

        session.invalidate();

        return "redirect:/login";
    }

    model.addAttribute("user", optionalUser.get());

    model.addAttribute("products",
            productRepository.findAll());

    model.addAttribute("categories",
            categoryRepository.findAll());

    System.out.println("Opening Home Page");

    return "home";
}
}