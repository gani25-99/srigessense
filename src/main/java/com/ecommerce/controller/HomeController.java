package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    // =========================================================
    // ROOT PAGE
    // /
    // =========================================================

    @GetMapping("/")
    public String index(
            @RequestParam(required = false) String keyword,
            Model model,
            HttpSession session) {

        // LOGIN CHECK
        String mobile =
                (String) session.getAttribute("mobile");

        if (mobile == null || mobile.isBlank()) {

            return "redirect:/login";
        }

        loadHomeData(keyword, model);

        return "home";
    }


    // =========================================================
    // HOME PAGE
    // /home
    // =========================================================

    @GetMapping("/home")
    public String home(
            @RequestParam(required = false) String keyword,
            Model model,
            HttpSession session) {

        // LOGIN CHECK
        String mobile =
                (String) session.getAttribute("mobile");

        if (mobile == null || mobile.isBlank()) {

            return "redirect:/login";
        }

        loadHomeData(keyword, model);

        return "home";
    }


    // =========================================================
    // COMMON HOME DATA
    // =========================================================

    private void loadHomeData(
            String keyword,
            Model model) {


        // =====================================================
        // SEARCH PRODUCTS
        // =====================================================

        if (keyword != null &&
                !keyword.trim().isEmpty()) {

            model.addAttribute(
                    "products",
                    productRepository
                            .searchProducts(keyword));

            model.addAttribute(
                    "keyword",
                    keyword);

        } else {

            model.addAttribute(
                    "products",
                    productRepository.findAll());
        }


        // =====================================================
        // BEST SELLERS
        // =====================================================

        model.addAttribute(
                "bestProducts",
                productRepository
                        .findBestSellerProducts());


        // =====================================================
        // NEW ARRIVALS
        // =====================================================

        model.addAttribute(
                "newProducts",
                productRepository
                        .findTop4ByOrderByIdDesc());


        // =====================================================
        // CATEGORIES
        // =====================================================

        model.addAttribute(
                "categories",
                categoryRepository.findAll());


        // =====================================================
        // BRANDS
        // =====================================================

        model.addAttribute(
                "brands",
                productRepository.findHomeBrands());
    }


    // =========================================================
    // LOGIN PAGE
    // =========================================================

    @GetMapping("/login")
    public String login() {

        return "login";
    }


    // =========================================================
    // REGISTER PAGE
    // =========================================================

    @GetMapping("/register")
    public String register() {

        return "register";
    }


    // =========================================================
    // FORGOT PASSWORD
    // =========================================================

    @GetMapping("/forgot-password")
    public String forgotPassword() {

        return "forgot-password";
    }


    // =========================================================
    // SUCCESS PAGE
    // =========================================================

    @GetMapping("/success")
    public String success() {

        return "success";
    }


    // =========================================================
    // LOGOUT
    // =========================================================

    @GetMapping("/logout")
    public String logout(
            HttpSession session) {

        // Destroy complete login session
        session.invalidate();

        // Redirect customer to login
        return "redirect:/login";
    }


    // =========================================================
    // ADMIN PRODUCTS
    // =========================================================

    @GetMapping("/admin/products")
    public String adminProducts() {

        return "admin-products";
    }
}