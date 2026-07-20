package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.entity.ContactMessage;
import com.ecommerce.service.ContactService;
import com.ecommerce.service.EmailService;

import jakarta.validation.Valid;

@Controller
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private EmailService emailService;

    // ==========================
    // Customer Contact Page
    // ==========================
    @GetMapping("/contact")
    public String contactPage(Model model) {

        model.addAttribute("contact", new ContactMessage());

        return "contact";
    }

    // ==========================
    // Save Contact Message
    // ==========================
    @PostMapping("/contact")
    public String saveMessage(
            @Valid @ModelAttribute("contact") ContactMessage contact,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "contact";
        }

        contactService.saveMessage(contact);

        model.addAttribute("success",
                "Thank you! Your message has been submitted successfully.");

        model.addAttribute("contact", new ContactMessage());

        return "contact";
    }

    // ==========================
    // Admin Contact Messages
    // ==========================
    @GetMapping("/admin/contact")
    public String adminMessages(
            @RequestParam(required = false) String keyword,
            Model model) {

        if (keyword != null && !keyword.trim().isEmpty()) {

            model.addAttribute("messages",
                    contactService.searchMessages(keyword));

        } else {

            model.addAttribute("messages",
                    contactService.getAllMessages());

        }

        model.addAttribute("keyword", keyword);

        // Dashboard Cards
        model.addAttribute("totalMessages",
                contactService.getTotalMessages());

        model.addAttribute("openMessages",
                contactService.getOpenMessages());

        model.addAttribute("resolvedMessages",
                contactService.getResolvedMessages());

        return "admin-contact";
    }

    // ==========================
    // View Contact Message
    // ==========================
    @GetMapping("/admin/contact/view/{id}")
    public String viewMessage(@PathVariable Long id,
                              Model model) {

        model.addAttribute("message",
                contactService.getMessageById(id));

        return "view-contact";
    }

    // ==========================
    // Reply Page
    // ==========================
    @GetMapping("/admin/contact/reply/{id}")
    public String replyPage(@PathVariable Long id,
                            Model model) {

        model.addAttribute("message",
                contactService.getMessageById(id));

        return "reply-contact";
    }

    // ==========================
    // Send Reply
    // ==========================
    @PostMapping("/admin/contact/sendReply")
    public String sendReply(@RequestParam Long id,
                            @RequestParam String email,
                            @RequestParam String subject,
                            @RequestParam String body,
                            RedirectAttributes redirectAttributes) {

        emailService.sendEmail(email, subject, body);

        contactService.markResolved(id);

        redirectAttributes.addFlashAttribute(
                "success",
                "Reply sent successfully.");

        return "redirect:/admin/contact";
    }

    // ==========================
    // Mark as Resolved
    // ==========================
    @GetMapping("/admin/contact/resolve/{id}")
    public String resolveMessage(@PathVariable Long id) {

        contactService.markResolved(id);

        return "redirect:/admin/contact";
    }

    // ==========================
    // Delete Contact Message
    // ==========================
    @GetMapping("/admin/contact/delete/{id}")
    public String deleteMessage(@PathVariable Long id) {

        contactService.deleteMessage(id);

        return "redirect:/admin/contact";
    }

}