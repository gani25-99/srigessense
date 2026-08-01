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
public class EmailLoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailOtpService emailOtpService;

    @PostMapping("/login/email")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            Model model) {

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {

            model.addAttribute("error", "Email not registered");

            return "login";
        }

        if (!user.get().getPassword().equals(password)) {

            model.addAttribute("error", "Invalid Password");

            return "login";
        }

        // Send Email OTP

        emailOtpService.sendOtp(email);

model.addAttribute("email", email);

model.addAttribute(
        "message",
        "OTP has been sent to your email.");

return "verify-email-otp";
    }

}