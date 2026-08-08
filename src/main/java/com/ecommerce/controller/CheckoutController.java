package com.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.Orders;
import com.ecommerce.entity.User;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.CheckoutService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private UserRepository userRepository;


    // =========================================================
    // CHECKOUT PAGE
    // =========================================================

    @GetMapping("/checkout")
    public String checkout(
            HttpSession session,
            Model model) {

        // =====================================================
        // LOGIN CHECK
        // =====================================================

        String loginMobile =
                (String) session.getAttribute("mobile");

        if (loginMobile == null ||
                loginMobile.isBlank()) {

            return "redirect:/login";
        }


        // =====================================================
        // FIND LOGGED-IN USER
        // =====================================================

        Optional<User> optionalUser =
                userRepository.findByMobile(loginMobile);

        if (optionalUser.isEmpty()) {

            session.invalidate();

            return "redirect:/login";
        }

        User user =
                optionalUser.get();


        // =====================================================
        // GET SELECTED CART IDS
        // =====================================================

        @SuppressWarnings("unchecked")
        List<Long> cartIds =
                (List<Long>) session.getAttribute(
                        "selectedCartIds");


        // =====================================================
        // MUST HAVE SELECTED PRODUCTS
        // =====================================================

        if (cartIds == null ||
                cartIds.isEmpty()) {

            return "redirect:/cart";
        }


        // =====================================================
        // GET ONLY THIS USER'S SELECTED CART ITEMS
        // =====================================================

        List<Cart> cartItems =
                checkoutService.getSelectedCart(
                        user,
                        cartIds);


        if (cartItems == null ||
                cartItems.isEmpty()) {

            session.removeAttribute(
                    "selectedCartIds");

            return "redirect:/cart";
        }


        // =====================================================
        // CALCULATE TOTAL
        // =====================================================

        double grandTotal =
                checkoutService.getSelectedGrandTotal(
                        user,
                        cartIds);


        // =====================================================
        // SEND DATA TO CHECKOUT PAGE
        // =====================================================

        model.addAttribute(
                "user",
                user);

        model.addAttribute(
                "cartItems",
                cartItems);

        model.addAttribute(
                "grandTotal",
                grandTotal);


        return "checkout";
    }


    // =========================================================
    // PLACE ORDER
    // =========================================================

    @PostMapping("/checkout/place-order")
    public String placeOrder(

            @RequestParam String customerName,

            @RequestParam String email,

            @RequestParam String mobile,

            @RequestParam String address,

            @RequestParam String city,

            @RequestParam String pincode,

            @RequestParam String paymentMethod,

            HttpSession session) {


        // =====================================================
        // LOGIN CHECK
        // =====================================================

        String loginMobile =
                (String) session.getAttribute("mobile");

        if (loginMobile == null ||
                loginMobile.isBlank()) {

            return "redirect:/login";
        }


        // =====================================================
        // FIND LOGGED-IN ACCOUNT OWNER
        // =====================================================

        Optional<User> optionalUser =
                userRepository.findByMobile(loginMobile);

        if (optionalUser.isEmpty()) {

            session.invalidate();

            return "redirect:/login";
        }

        User user =
                optionalUser.get();


        // =====================================================
        // GET SELECTED CART IDS
        // =====================================================

        @SuppressWarnings("unchecked")
        List<Long> cartIds =
                (List<Long>) session.getAttribute(
                        "selectedCartIds");


        // =====================================================
        // CHECK SELECTION
        // =====================================================

        if (cartIds == null ||
                cartIds.isEmpty()) {

            return "redirect:/cart";
        }


        // =====================================================
        // VERIFY CART BELONGS TO LOGGED-IN USER
        // =====================================================

        List<Cart> selectedItems =
                checkoutService.getSelectedCart(
                        user,
                        cartIds);


        if (selectedItems == null ||
                selectedItems.isEmpty()) {

            session.removeAttribute(
                    "selectedCartIds");

            return "redirect:/cart";
        }


        // =====================================================
        // CREATE ORDER
        //
        // user = ACCOUNT OWNER
        //
        // customerName/email/mobile/address/city/pincode
        // = RECIPIENT DETAILS FOR THIS ORDER
        // =====================================================

        Orders order =
                checkoutService.placeOrder(

                        user,

                        cartIds,

                        customerName,

                        email,

                        mobile,

                        address,

                        city,

                        pincode,

                        paymentMethod);


        // =====================================================
        // CLEAR SELECTED CART IDS
        // =====================================================

        session.removeAttribute(
                "selectedCartIds");


        // =====================================================
        // ONLINE PAYMENT
        // =====================================================

        if (!"COD".equalsIgnoreCase(
                paymentMethod)) {

            return "redirect:/payment/"
                    + order.getId();
        }


        // =====================================================
        // COD ORDER
        // =====================================================

        return "redirect:/order/"
                + order.getId();
    }


    // =========================================================
    // ORDER SUCCESS PAGE
    // =========================================================

    @GetMapping("/order-success")
    public String orderSuccess() {

        return "order-success";
    }
}