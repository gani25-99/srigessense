package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ecommerce.service.CartService;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/cart")
    public String cart(Model model) {

        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("grandTotal", cartService.getGrandTotal());

        return "cart";
    }

    @GetMapping("/cart/add/{id}")
    public String addToCart(@PathVariable Long id) {

        try {
            cartService.addToCart(id);
        } catch (RuntimeException e) {
            return "redirect:/product/" + id + "?error=outofstock";
        }

        return "redirect:/cart";
    }

    @GetMapping("/cart/increase/{id}")
    public String increase(@PathVariable Long id) {

        cartService.increaseQuantity(id);

        return "redirect:/cart";
    }

    @GetMapping("/cart/decrease/{id}")
    public String decrease(@PathVariable Long id) {

        cartService.decreaseQuantity(id);

        return "redirect:/cart";
    }

    @GetMapping("/cart/delete/{id}")
    public String delete(@PathVariable Long id) {

        cartService.remove(id);

        return "redirect:/cart";
    }

}