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

    @PostMapping("/forgot-password/send-otp")
    public String sendOtp(
            @RequestParam String email,
            Model model) {

        Optional<User> user =
                userRepository.findByEmail(email);

        if(user.isEmpty()){

            model.addAttribute(
                    "error",
                    "Email not registered");

            return "forgot-password";

        }

        emailOtpService.sendOtp(email);

        model.addAttribute("email",email);

        return "reset-password";

    }

}