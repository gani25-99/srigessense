package com.ecommerce.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.entity.User;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.EmailOtpService;

import jakarta.servlet.http.HttpSession;

@Controller
public class VerifyEmailOtpController {

    @Autowired
    private EmailOtpService emailOtpService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login/verify-email-otp")
    public String verifyOtp(
            @RequestParam String email,
            @RequestParam String otp,
            HttpSession session,
            Model model) {

        boolean verified = emailOtpService.verifyOtp(email, otp);

        if (!verified) {

            model.addAttribute("email", email);
            model.addAttribute("error", "Invalid OTP");

            return "verify-email-otp";
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {

            model.addAttribute("error", "User not found");

            return "login";
        }

        User user = optionalUser.get();

        // Create login session
        session.setAttribute("email", user.getEmail());
        session.setAttribute("mobile", user.getMobile());

        return "redirect:/home";
    }
}