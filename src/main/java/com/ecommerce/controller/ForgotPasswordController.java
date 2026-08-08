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

@Controller
public class ForgotPasswordController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailOtpService emailOtpService;

    // =====================================
    // SEND EMAIL OTP
    // =====================================

    @PostMapping("/forgot-password/send-otp")
    public String sendOtp(
            @RequestParam String email,
            Model model) {

        Optional<User> user =
                userRepository.findByEmail(email);

        if (user.isEmpty()) {

            model.addAttribute(
                    "error",
                    "Email not registered.");

            return "forgot-password";
        }

        emailOtpService.sendOtp(email);

        model.addAttribute("email", email);

        return "reset-password";
    }

    // =====================================
    // RESET PASSWORD
    // =====================================

    @PostMapping("/forgot-password/reset")
    public String resetPassword(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String password,
            Model model) {

        boolean verified =
                emailOtpService.verifyOtp(email, otp);

        if (!verified) {

            model.addAttribute("email", email);
            model.addAttribute("error", "Invalid OTP.");

            return "reset-password";
        }

        Optional<User> user =
                userRepository.findByEmail(email);

        if (user.isEmpty()) {

            model.addAttribute(
                    "error",
                    "User not found.");

            return "forgot-password";
        }

        User existingUser = user.get();

        existingUser.setPassword(password);

        userRepository.save(existingUser);

        model.addAttribute(
                "message",
                "Password changed successfully. Please login.");

        return "login";
    }

}