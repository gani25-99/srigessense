package com.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.entity.Orders;
import com.ecommerce.repository.OrdersRepository;

@Service
public class AdminOrderService {

    @Autowired
    private OrdersRepository ordersRepository;

    // Get all orders
    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }

    // Get order by ID
    public Orders getOrderById(Long id) {
        return ordersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // Update order
    public Orders saveOrder(Orders order) {
        return ordersRepository.save(order);
    }

    // Delete order
    public void deleteOrder(Long id) {
        ordersRepository.deleteById(id);
    }
}