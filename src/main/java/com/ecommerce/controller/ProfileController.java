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

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {

        String mobile = (String) session.getAttribute("mobile");

        if (mobile == null) {
            return "redirect:/";
        }

        Optional<User> optional = userRepository.findByMobile(mobile);

        if (optional.isEmpty()) {
            return "redirect:/";
        }

        model.addAttribute("user", optional.get());

        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(

            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String address,
            @RequestParam(value = "image", required = false) MultipartFile image,
            HttpSession session,
            Model model) throws Exception {

        String mobile = (String) session.getAttribute("mobile");

        if (mobile == null) {
            return "redirect:/";
        }

        Optional<User> optional = userRepository.findByMobile(mobile);

        if (optional.isPresent()) {

            User user = optional.get();

            user.setName(name);
            user.setEmail(email);
            user.setAddress(address);

            if (image != null && !image.isEmpty()) {

                String fileName = fileUploadService.uploadProfileImage(image);
                user.setProfileImage(fileName);
            }

            userRepository.save(user);

            model.addAttribute("user", user);
            model.addAttribute("success", "Profile Updated Successfully!");
        }

        return "profile";
    }
}