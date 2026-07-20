package com.ecommerce.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ecommerce.entity.User;
import com.ecommerce.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserDashboardController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        String mobile = (String) session.getAttribute("mobile");

        if (mobile == null) {
            return "redirect:/";
        }

        Optional<User> user = userRepository.findByMobile(mobile);

        if (user.isEmpty()) {
            return "redirect:/";
        }

        model.addAttribute("user", user.get());

        return "dashboard";
    }
}