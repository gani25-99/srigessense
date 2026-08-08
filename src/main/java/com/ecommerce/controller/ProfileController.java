package com.ecommerce.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.entity.User;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.FileUploadService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileUploadService fileUploadService;

    // =====================================
    // MY PROFILE
    // =====================================

    @GetMapping("/profile")
    public String profile(
            HttpSession session,
            Model model) {

        String mobile =
                (String) session.getAttribute("mobile");

        if (mobile == null) {
            return "redirect:/login";
        }

        Optional<User> optionalUser =
                userRepository.findByMobile(mobile);

        if (optionalUser.isEmpty()) {

            session.invalidate();

            return "redirect:/login";
        }

        model.addAttribute(
                "user",
                optionalUser.get());

        return "profile";
    }

    // =====================================
    // UPDATE PROFILE
    // =====================================

    @PostMapping("/profile/update")
    public String updateProfile(

            @RequestParam String name,

            @RequestParam String email,

            @RequestParam String address,

            @RequestParam(
                    value = "image",
                    required = false)
            MultipartFile image,

            HttpSession session,

            Model model) throws Exception {

        String mobile =
                (String) session.getAttribute("mobile");

        if (mobile == null) {
            return "redirect:/login";
        }

        Optional<User> optionalUser =
                userRepository.findByMobile(mobile);

        if (optionalUser.isEmpty()) {

            session.invalidate();

            return "redirect:/login";
        }

        User user = optionalUser.get();

        // ==========================
        // UPDATE PROFILE DETAILS
        // ==========================

        user.setName(name);
        user.setEmail(email);
        user.setAddress(address);

        // ==========================
        // PROFILE IMAGE
        // ==========================

        if (image != null && !image.isEmpty()) {

            String fileName =
                    fileUploadService
                            .uploadProfileImage(image);

            user.setProfileImage(fileName);
        }

        // ==========================
        // SAVE
        // ==========================

        userRepository.save(user);

        model.addAttribute(
                "user",
                user);

        model.addAttribute(
                "message",
                "Profile updated successfully.");

        return "profile";
    }
}