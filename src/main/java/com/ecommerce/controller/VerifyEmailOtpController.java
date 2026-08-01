package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.service.EmailOtpService;

@Controller
public class VerifyEmailOtpController {

    @Autowired
    private EmailOtpService emailOtpService;

    @PostMapping("/login/verify-email-otp")
    public String verifyOtp(
            @RequestParam String email,
            @RequestParam String otp,
            Model model) {

        boolean verified = emailOtpService.verifyOtp(email, otp);

        if (!verified) {

            model.addAttribute("email", email);
            model.addAttribute("error", "Invalid OTP");

            return "verify-email-otp";
        }

        return "redirect:/home";
    }

}