package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.service.OtpService;

import jakarta.servlet.http.HttpSession;

@Controller
public class OtpController {

    @Autowired
    private OtpService otpService;

    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam String mobile, Model model) {

        otpService.sendOtp(mobile);

        model.addAttribute("mobile", mobile);

        return "verify-otp";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String mobile,
                            @RequestParam String otp,
                            Model model,
                            HttpSession session) {

        String result = otpService.verifyOtp(mobile, otp);

        if (result.equals("Login Successful") ||
            result.equals("New User Registered Successfully")) {

            session.setAttribute("mobile", mobile);

            return "redirect:/home";
        }

        model.addAttribute("mobile", mobile);
        model.addAttribute("error", "Invalid OTP");

        return "verify-otp";
    }
}