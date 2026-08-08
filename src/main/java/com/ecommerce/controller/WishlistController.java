package com.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.entity.Wishlist;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.CartService;
import com.ecommerce.service.WishlistService;

import jakarta.servlet.http.HttpSession;

@Controller
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;


       @Autowired
private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    // ==========================
    // VIEW WISHLIST
    // ==========================

    @GetMapping("/wishlist")
    public String wishlist(
            HttpSession session,
            Model model) {

        String mobile = (String) session.getAttribute("mobile");

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

        List<Wishlist> wishlist =
                wishlistService.getWishlist(user);

        model.addAttribute("wishlist", wishlist);

        return "wishlist";
    }

    // ==========================
    // ADD TO WISHLIST
    // ==========================

    @GetMapping("/wishlist/add/{id}")
    public String addWishlist(
            @PathVariable Long id,
            HttpSession session) {

        String mobile = (String) session.getAttribute("mobile");

        if (mobile == null) {
            return "redirect:/login";
        }

        Optional<User> optionalUser =
                userRepository.findByMobile(mobile);

        Optional<Product> optionalProduct =
                productRepository.findById(id);

        if (optionalUser.isEmpty() || optionalProduct.isEmpty()) {
            return "redirect:/home";
        }

        wishlistService.add(
                optionalUser.get(),
                optionalProduct.get());

        return "redirect:/wishlist";
    }

    // ==========================
    // REMOVE FROM WISHLIST
    // ==========================

    @GetMapping("/wishlist/remove/{id}")
    public String removeWishlist(
            @PathVariable Long id,
            HttpSession session) {

        String mobile = (String) session.getAttribute("mobile");

        if (mobile == null) {
            return "redirect:/login";
        }

        Optional<User> optionalUser =
                userRepository.findByMobile(mobile);

        Optional<Product> optionalProduct =
                productRepository.findById(id);

        if (optionalUser.isEmpty() || optionalProduct.isEmpty()) {
            return "redirect:/wishlist";
        }

        wishlistService.remove(
                optionalUser.get(),
                optionalProduct.get());

        return "redirect:/wishlist";
    }
 
// ==========================
// MOVE TO CART
// ==========================

@GetMapping("/wishlist/move-to-cart/{id}")
public String moveToCart(@PathVariable Long id,
                         HttpSession session) {

    String mobile = (String) session.getAttribute("mobile");

    if (mobile == null) {
        return "redirect:/login";
    }

    Optional<User> optionalUser =
            userRepository.findByMobile(mobile);

    Optional<Product> optionalProduct =
            productRepository.findById(id);

    if (optionalUser.isEmpty() || optionalProduct.isEmpty()) {
        return "redirect:/wishlist";
    }

    User user = optionalUser.get();
    Product product = optionalProduct.get();

    cartService.add(user, product);

    wishlistService.remove(user, product);

    return "redirect:/cart";
}

}