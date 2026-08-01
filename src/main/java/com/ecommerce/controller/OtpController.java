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
    public String sendOtp(
            @RequestParam String name,
            @RequestParam(required = false) String email,
            @RequestParam String mobile,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        otpService.sendOtp(mobile);

        session.setAttribute("name", name);
        session.setAttribute("email", email);
        session.setAttribute("mobile", mobile);
        session.setAttribute("password", password);

        model.addAttribute("mobile", mobile);

        return "verify-otp";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(
            @RequestParam String mobile,
            @RequestParam String otp,
            HttpSession session,
            Model model) {

        String name = (String) session.getAttribute("name");
        String email = (String) session.getAttribute("email");
        String password = (String) session.getAttribute("password");

        String result = otpService.verifyOtp(
                name,
                email,
                mobile,
                password,
                otp);

        if ("Success".equals(result)) {
            session.setAttribute("mobile", mobile);
            return "redirect:/home";
        }

        model.addAttribute("mobile", mobile);
        model.addAttribute("error", "Invalid OTP");

        return "verify-otp";
    }
}