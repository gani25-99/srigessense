package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ecommerce.entity.Product;
import com.ecommerce.entity.Review;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.ReviewService;

@Controller
public class ReviewController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewService reviewService;

    // Product Details Page
    @GetMapping("/product/{id}")
    public String productDetails(@PathVariable Long id, Model model) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product Not Found"));

        model.addAttribute("product", product);
        model.addAttribute("reviews", reviewService.getReviews(id));
        model.addAttribute("review", new Review());

        return "product-details";
    }

    // Save Review
    @PostMapping("/review/{id}")
    public String saveReview(@PathVariable Long id,
                             @ModelAttribute Review review) {

        reviewService.saveReview(id, review);

        return "redirect:/product/" + id;
    }

}