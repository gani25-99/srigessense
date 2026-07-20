package com.ecommerce.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.Orders;
import com.ecommerce.entity.Product;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.OrderItemRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    // Place Order
    public Orders placeOrder(Orders order) {

        List<Cart> cartItems = cartRepository.findAll();

        double total = 0.0;

        // Calculate Total
        for (Cart item : cartItems) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }

        order.setTotalAmount(total);

        // Initial Tracking
        order.setStatus("PLACED");
        order.setPlacedAt(LocalDateTime.now());
        order.setExpectedDeliveryDate(LocalDate.now().plusDays(5));

        // Save Order
        Orders savedOrder = orderRepository.save(order);

        // Save Order Items & Reduce Stock
        for (Cart item : cartItems) {

            Product product = productRepository
                    .findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product Not Found"));

            // Check Stock
            // Check Stock
if (product.getQuantity() < item.getQuantity()) {
    throw new RuntimeException(
            product.getName() + " has only "
            + product.getQuantity() + " item(s) left.");
}

// Reduce Stock
product.setQuantity(product.getQuantity() - item.getQuantity());

productRepository.save(product);

            // Save Order Item
            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(product.getPrice());

            orderItemRepository.save(orderItem);
        }

        // Clear Cart
        cartRepository.deleteAll();

        return savedOrder;
    }

    // Get All Orders
    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    // Get Order By ID
    public Orders getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order Not Found"));
    }

    // Update Order Status
    public void updateStatus(Long id, String status) {

        Orders order = getOrderById(id);

        order.setStatus(status);

        switch (status) {

            case "CONFIRMED":
                if (order.getConfirmedAt() == null) {
                    order.setConfirmedAt(LocalDateTime.now());
                }
                break;

            case "PACKED":
                if (order.getPackedAt() == null) {
                    order.setPackedAt(LocalDateTime.now());
                }
                break;

            case "SHIPPED":
                if (order.getShippedAt() == null) {
                    order.setShippedAt(LocalDateTime.now());
                }
                break;

            case "OUT FOR DELIVERY":
                if (order.getOutForDeliveryAt() == null) {
                    order.setOutForDeliveryAt(LocalDateTime.now());
                }
                break;

            case "DELIVERED":
                if (order.getDeliveredAt() == null) {
                    order.setDeliveredAt(LocalDateTime.now());
                }
                break;

            default:
                break;
        }

        orderRepository.save(order);
    }

    // Update Courier Details
    public void updateCourierDetails(Long id,
                                     String courierName,
                                     String trackingNumber) {

        Orders order = getOrderById(id);

        order.setCourierName(courierName);
        order.setTrackingNumber(trackingNumber);

        orderRepository.save(order);
    }

    // Total Revenue
    public Double getRevenue() {

        Double revenue = orderRepository.getTotalRevenue();

        return revenue == null ? 0.0 : revenue;
    }
}