package com.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.CartService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;


    // =========================================================
    // VIEW CART
    // =========================================================

    @GetMapping("/cart")
    public String cart(
            HttpSession session,
            Model model) {

        String mobile =
                (String) session.getAttribute("mobile");

        // LOGIN CHECK
        if (mobile == null || mobile.isBlank()) {

            return "redirect:/login";
        }


        // FIND USER
        Optional<User> optionalUser =
                userRepository.findByMobile(mobile);

        if (optionalUser.isEmpty()) {

            session.invalidate();

            return "redirect:/login";
        }


        User user =
                optionalUser.get();


        // GET CART
        List<Cart> cartItems =
                cartService.getCart(user);


        // CALCULATE TOTAL
        double grandTotal =
                cartItems.stream()
                        .mapToDouble(item ->
                                item.getProduct()
                                        .getPrice()
                                        * item.getQuantity())
                        .sum();


        model.addAttribute(
                "cartItems",
                cartItems);

        model.addAttribute(
                "grandTotal",
                grandTotal);


        return "cart";
    }


    // =========================================================
    // ADD TO CART
    // =========================================================

    @GetMapping("/cart/add/{id}")
    public String addToCart(
            @PathVariable Long id,
            HttpSession session) {

        String mobile =
                (String) session.getAttribute("mobile");


        // LOGIN CHECK
        if (mobile == null || mobile.isBlank()) {

            return "redirect:/login";
        }


        Optional<User> optionalUser =
                userRepository.findByMobile(mobile);

        Optional<Product> optionalProduct =
                productRepository.findById(id);


        if (optionalUser.isEmpty()) {

            session.invalidate();

            return "redirect:/login";
        }


        if (optionalProduct.isEmpty()) {

            return "redirect:/home";
        }


        cartService.add(
                optionalUser.get(),
                optionalProduct.get());


        return "redirect:/cart";
    }


    // =========================================================
    // INCREASE QUANTITY
    // =========================================================

    @GetMapping("/cart/increase/{id}")
    public String increase(
            @PathVariable Long id,
            HttpSession session) {

        String mobile =
                (String) session.getAttribute("mobile");


        // LOGIN CHECK
        if (mobile == null || mobile.isBlank()) {

            return "redirect:/login";
        }


        Optional<User> optionalUser =
                userRepository.findByMobile(mobile);

        Optional<Product> optionalProduct =
                productRepository.findById(id);


        if (optionalUser.isEmpty()) {

            session.invalidate();

            return "redirect:/login";
        }


        if (optionalProduct.isPresent()) {

            cartService.increase(
                    optionalUser.get(),
                    optionalProduct.get());
        }


        return "redirect:/cart";
    }


    // =========================================================
    // DECREASE QUANTITY
    // =========================================================

    @GetMapping("/cart/decrease/{id}")
    public String decrease(
            @PathVariable Long id,
            HttpSession session) {

        String mobile =
                (String) session.getAttribute("mobile");


        // LOGIN CHECK
        if (mobile == null || mobile.isBlank()) {

            return "redirect:/login";
        }


        Optional<User> optionalUser =
                userRepository.findByMobile(mobile);

        Optional<Product> optionalProduct =
                productRepository.findById(id);


        if (optionalUser.isEmpty()) {

            session.invalidate();

            return "redirect:/login";
        }


        if (optionalProduct.isPresent()) {

            cartService.decrease(
                    optionalUser.get(),
                    optionalProduct.get());
        }


        return "redirect:/cart";
    }


    // =========================================================
    // REMOVE FROM CART
    // =========================================================

    @GetMapping("/cart/remove/{id}")
    public String remove(
            @PathVariable Long id,
            HttpSession session) {

        String mobile =
                (String) session.getAttribute("mobile");


        // LOGIN CHECK
        if (mobile == null || mobile.isBlank()) {

            return "redirect:/login";
        }


        Optional<User> optionalUser =
                userRepository.findByMobile(mobile);

        Optional<Product> optionalProduct =
                productRepository.findById(id);


        if (optionalUser.isEmpty()) {

            session.invalidate();

            return "redirect:/login";
        }


        if (optionalProduct.isPresent()) {

            cartService.remove(
                    optionalUser.get(),
                    optionalProduct.get());
        }


        return "redirect:/cart";
    }


    // =========================================================
    // CHECKOUT SELECTED ITEMS
    // =========================================================

    @PostMapping("/cart/checkout-selected")
    public String checkoutSelected(
            @RequestParam("cartIds")
            List<Long> cartIds,
            HttpSession session) {


        // LOGIN CHECK
        String mobile =
                (String) session.getAttribute("mobile");


        if (mobile == null || mobile.isBlank()) {

            return "redirect:/login";
        }


        // NO PRODUCTS SELECTED
        if (cartIds == null ||
                cartIds.isEmpty()) {

            return "redirect:/cart";
        }


        // SAVE SELECTED CART IDS
        session.setAttribute(
                "selectedCartIds",
                cartIds);


        // GO TO CHECKOUT
        return "redirect:/checkout";
    }
}