package com.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChangePasswordController {

    @GetMapping("/change-password")
    public String changePasswordPage() {
        return "change-password";
    }

}