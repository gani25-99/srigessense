package com.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.entity.ContactMessage;
import com.ecommerce.service.ContactService;

@Controller
public class ComplaintController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/my-complaints")
    public String myComplaints(
            @RequestParam(required = false) String email,
            Model model) {

        if (email != null && !email.isBlank()) {
            List<ContactMessage> complaints =
                    contactService.getMyComplaints(email);

            model.addAttribute("complaints", complaints);
        }

        model.addAttribute("email", email);

        return "my-complaints";
    }
}