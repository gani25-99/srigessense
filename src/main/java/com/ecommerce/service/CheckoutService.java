package com.ecommerce.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.controller.InvoiceController;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.Orders;
import com.ecommerce.entity.User;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.OrderItemRepository;
import com.ecommerce.repository.OrdersRepository;

@Service
public class CheckoutService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private InvoiceController invoiceController;


    // =========================================================
    // GET CART
    // =========================================================

    public List<Cart> getCart(User user) {

        return cartRepository.findByUser(user);
    }


    // =========================================================
    // GET SELECTED CART ITEMS
    // =========================================================

    public List<Cart> getSelectedCart(
            User user,
            List<Long> cartIds) {

        if (cartIds == null || cartIds.isEmpty()) {

            return new ArrayList<>();
        }

        return cartRepository.findByUser(user)
                .stream()
                .filter(cart ->
                        cartIds.contains(cart.getId()))
                .collect(Collectors.toList());
    }


    // =========================================================
    // COMPLETE CART GRAND TOTAL
    // =========================================================

    public double getGrandTotal(User user) {

        return cartRepository.findByUser(user)
                .stream()
                .mapToDouble(item ->
                        item.getProduct().getPrice()
                                * item.getQuantity())
                .sum();
    }


    // =========================================================
    // SELECTED GRAND TOTAL
    // =========================================================

    public double getSelectedGrandTotal(
            User user,
            List<Long> cartIds) {

        return getSelectedCart(user, cartIds)
                .stream()
                .mapToDouble(item ->
                        item.getProduct().getPrice()
                                * item.getQuantity())
                .sum();
    }


    // =========================================================
    // PLACE ORDER
    // =========================================================

    public Orders placeOrder(

            User user,

            List<Long> cartIds,

            String address,

            String city,

            String pincode,

            String paymentMethod) {


        // =====================================================
        // GET ONLY SELECTED ITEMS
        // =====================================================

        List<Cart> cartItems =
                getSelectedCart(
                        user,
                        cartIds);


        if (cartItems.isEmpty()) {

            throw new RuntimeException(
                    "Selected cart is empty.");
        }


        // =====================================================
        // CREATE ORDER
        // =====================================================

        Orders order =
                new Orders();


        order.setCustomerName(
                user.getName());

        order.setEmail(
                user.getEmail());

        order.setMobile(
                user.getMobile());

        order.setAddress(
                address);

        order.setCity(
                city);

        order.setPincode(
                pincode);

        order.setStatus(
                "Pending");

        order.setPlacedAt(
                LocalDateTime.now());

        order.setExpectedDeliveryDate(
                LocalDate.now().plusDays(5));

        order.setPaymentMethod(
                paymentMethod);


        // =====================================================
        // PAYMENT STATUS
        // =====================================================

        order.setPaymentStatus(
                "PENDING");


        // =====================================================
        // SELECTED TOTAL
        // =====================================================

        double total =
                cartItems.stream()
                        .mapToDouble(item ->
                                item.getProduct()
                                        .getPrice()
                                        * item.getQuantity())
                        .sum();


        order.setTotalAmount(
                total);


        // =====================================================
        // SAVE ORDER
        // =====================================================

        Orders savedOrder =
                ordersRepository.save(order);


        // =====================================================
        // SAVE ORDER ITEMS
        // =====================================================

        for (Cart cart : cartItems) {

            OrderItem item =
                    new OrderItem();

            item.setOrder(
                    savedOrder);

            item.setProduct(
                    cart.getProduct());

            item.setQuantity(
                    cart.getQuantity());

            item.setPrice(
                    cart.getProduct()
                            .getPrice());

            orderItemRepository.save(
                    item);
        }


        // =====================================================
        // REMOVE ONLY SELECTED ITEMS
        // =====================================================

        cartRepository.deleteAll(
                cartItems);


        // =====================================================
        // COD
        // SEND EMAIL NOW
        // =====================================================

        if ("COD".equalsIgnoreCase(
                paymentMethod)) {

            sendInvoiceEmail(
                    savedOrder);
        }


        // =====================================================
        // ONLINE
        //
        // DO NOT SEND EMAIL HERE.
        //
        // Payment controller will call
        // sendInvoiceEmail() after successful payment.
        // =====================================================


        return savedOrder;
    }


    // =========================================================
    // PLACE ORDER WITH RECIPIENT DETAILS
    // =========================================================

    public Orders placeOrder(

            User user,

            List<Long> cartIds,

            String recipientName,

            String recipientEmail,

            String recipientMobile,

            String address,

            String city,

            String pincode,

            String paymentMethod) {

        List<Cart> cartItems =
                getSelectedCart(
                        user,
                        cartIds);

        if (cartItems.isEmpty()) {

            throw new RuntimeException(
                    "Selected cart is empty.");
        }

        Orders order =
                new Orders();

        // Account owner: the logged-in customer.
        order.setUser(user);

        // Recipient details: these may belong to another person.
        order.setCustomerName(recipientName);
        order.setEmail(recipientEmail);
        order.setMobile(recipientMobile);
        order.setAddress(address);
        order.setCity(city);
        order.setPincode(pincode);

        order.setStatus("Pending");
        order.setPlacedAt(LocalDateTime.now());
        order.setExpectedDeliveryDate(
                LocalDate.now().plusDays(5));
        order.setPaymentMethod(paymentMethod);
        order.setPaymentStatus("PENDING");

        double total =
                cartItems.stream()
                        .mapToDouble(item ->
                                item.getProduct().getPrice()
                                        * item.getQuantity())
                        .sum();

        order.setTotalAmount(total);

        Orders savedOrder =
                ordersRepository.save(order);

        for (Cart cart : cartItems) {

            OrderItem item =
                    new OrderItem();

            item.setOrder(savedOrder);
            item.setProduct(cart.getProduct());
            item.setQuantity(cart.getQuantity());
            item.setPrice(
                    cart.getProduct().getPrice());

            orderItemRepository.save(item);
        }

        cartRepository.deleteAll(cartItems);

        if ("COD".equalsIgnoreCase(paymentMethod)) {

            sendInvoiceEmail(savedOrder);
        }

        return savedOrder;
    }


    // =========================================================
    // SEND INVOICE EMAIL
    //
    // CALL THIS ONLY AFTER PAYMENT SUCCESS FOR ONLINE PAYMENT
    // =========================================================

    public void sendInvoiceEmail(
            Orders order) {

        try {

            // =================================================
            // GENERATE BRANDED PDF
            // =================================================

            byte[] invoicePdf =
                    invoiceController
                            .generateInvoicePdf(
                                    order);


            // =================================================
            // EMAIL SUBJECT
            // =================================================

            String subject =
                    "SRIG ESSENSE - Order Confirmation #"
                            + order.getId();


            // =================================================
            // EMAIL BODY
            // =================================================

            StringBuilder body =
                    new StringBuilder();


            body.append(
                    "Dear ")
                    .append(
                            order.getCustomerName())
                    .append(",\n\n");


            body.append(
                    "Thank you for shopping with "
                    + "SRIG ESSENSE.\n\n");


            body.append(
                    "Your order has been successfully "
                    + "placed.\n\n");


            body.append(
                    "ORDER INFORMATION\n");

            body.append(
                    "--------------------------------\n");


            body.append(
                    "Order ID : #")
                    .append(
                            order.getId())
                    .append("\n");


            body.append(
                    "Invoice No : INV-")
                    .append(
                            order.getId())
                    .append("\n");


            body.append(
                    "Order Date : ")
                    .append(
                            order.getPlacedAt())
                    .append("\n");


            body.append(
                    "Order Status : ")
                    .append(
                            order.getStatus())
                    .append("\n");


            body.append(
                    "Payment Method : ")
                    .append(
                            order.getPaymentMethod())
                    .append("\n");


            body.append(
                    "Payment Status : ")
                    .append(
                            order.getPaymentStatus())
                    .append("\n");


            body.append(
                    "Grand Total : ₹")
                    .append(
                            String.format(
                                    "%.2f",
                                    order.getTotalAmount()))
                    .append("\n\n");


            body.append(
                    "Expected Delivery : ")
                    .append(
                            order.getExpectedDeliveryDate())
                    .append("\n\n");


            body.append(
                    "Your branded invoice is attached "
                    + "to this email as a PDF.\n\n");


            body.append(
                    "Thank you for choosing "
                    + "SRIG ESSENSE.\n");


            body.append(
                    "Own Your Style\n\n");


            body.append(
                    "SRIG ESSENSE\n");

            body.append(
                    "Kalyandurg, Ananthapur\n");

            body.append(
                    "Andhra Pradesh - 515761\n");

            body.append(
                    "+91 9392694740\n");

            body.append(
                    "support@srigessense.com\n");

            body.append(
                    "www.srig.com");


            // =================================================
            // SEND
            // =================================================

            emailService.sendEmail(

                    order.getEmail(),

                    subject,

                    body.toString(),

                    invoicePdf
            );


            System.out.println(
                    "================================");

            System.out.println(
                    "SRIG INVOICE EMAIL SENT");

            System.out.println(
                    "Order ID : "
                            + order.getId());

            System.out.println(
                    "Customer : "
                            + order.getEmail());

            System.out.println(
                    "Payment : "
                            + order.getPaymentStatus());

            System.out.println(
                    "================================");


        } catch (Exception e) {

            System.out.println(
                    "Invoice email failed for order "
                            + order.getId());

            e.printStackTrace();
        }
    }
}