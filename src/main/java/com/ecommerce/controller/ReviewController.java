package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ecommerce.entity.Review;
import com.ecommerce.service.ReviewService;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // ==========================
    // SAVE REVIEW
    // ==========================

    @PostMapping("/review/{id}")
    public String saveReview(@PathVariable Long id,
                             @ModelAttribute Review review) {

        reviewService.saveReview(id, review);

        return "redirect:/product/" + id;

    }

}