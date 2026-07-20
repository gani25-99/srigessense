package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ecommerce.service.WishlistService;

@Controller
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @GetMapping("/wishlist")
    public String wishlist(Model model) {

        model.addAttribute("items", wishlistService.getAll());

        return "wishlist";

    }

    @GetMapping("/wishlist/add/{id}")
    public String add(@PathVariable Long id) {

        wishlistService.add(id);

        return "redirect:/wishlist";

    }

    @GetMapping("/wishlist/delete/{id}")
    public String delete(@PathVariable Long id) {

        wishlistService.remove(id);

        return "redirect:/wishlist";

    }

}