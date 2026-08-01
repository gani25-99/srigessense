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

    // ==========================
    // LOGIN - SEND OTP
    // ==========================

    @PostMapping("/login/send-otp")
    public String loginSendOtp(
            @RequestParam String mobile,
            Model model) {

        otpService.sendOtp(mobile);

        model.addAttribute("mobile", mobile);

        return "verify-otp";
    }

    // ==========================
    // LOGIN - VERIFY OTP
    // ==========================

    @PostMapping("/login/verify-otp")
    public String loginVerifyOtp(
            @RequestParam String mobile,
            @RequestParam String otp,
            HttpSession session,
            Model model) {

        String result = otpService.verifyLoginOtp(mobile, otp);

        if ("Success".equals(result)) {

            session.setAttribute("mobile", mobile);

            return "redirect:/home";
        }

        model.addAttribute("mobile", mobile);
        model.addAttribute("error", result);

        return "verify-otp";
    }

    // ==========================
    // REGISTER - SEND OTP
    // ==========================

    @PostMapping("/register/send-otp")
    public String registerSendOtp(
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

    // ==========================
    // REGISTER - VERIFY OTP
    // ==========================

    @PostMapping("/register/verify-otp")
    public String registerVerifyOtp(
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
        model.addAttribute("error", result);

        return "verify-login-otp";
    }

}