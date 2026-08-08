package com.ecommerce.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.entity.Orders;
import com.ecommerce.repository.OrdersRepository;

@Service
public class OrderService {

    @Autowired
    private OrdersRepository ordersRepository;

    // ==========================
    // GET ALL ORDERS
    // ==========================

    public List<Orders> getAllOrders() {

        return ordersRepository.findAll();

    }

    // ==========================
    // GET ORDER BY ID
    // ==========================

    public Orders getOrderById(Long id) {

        return ordersRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order Not Found"));

    }

    // ==========================
    // UPDATE STATUS
    // ==========================

    public void updateStatus(Long id, String status) {

        Orders order = getOrderById(id);

        order.setStatus(status);

        LocalDateTime now = LocalDateTime.now();

        switch (status) {

            case "Confirmed":
                if (order.getConfirmedAt() == null)
                    order.setConfirmedAt(now);
                break;

            case "Packed":
                if (order.getPackedAt() == null)
                    order.setPackedAt(now);
                break;

            case "Shipped":
                if (order.getShippedAt() == null)
                    order.setShippedAt(now);
                break;

            case "Out For Delivery":
                if (order.getOutForDeliveryAt() == null)
                    order.setOutForDeliveryAt(now);
                break;

            case "Delivered":
                if (order.getDeliveredAt() == null)
                    order.setDeliveredAt(now);
                break;

            default:
                break;
        }

        ordersRepository.save(order);

    }

    // ==========================
    // UPDATE COURIER
    // ==========================

    public void updateCourierDetails(
            Long id,
            String courierName,
            String trackingNumber) {

        Orders order = getOrderById(id);

        order.setCourierName(courierName);
        order.setTrackingNumber(trackingNumber);

        ordersRepository.save(order);

    }

    // ==========================
    // TOTAL REVENUE
    // ==========================

    public Double getRevenue() {

        Double revenue = ordersRepository.getTotalRevenue();

        return revenue == null ? 0.0 : revenue;

    }
    // ==========================
// TOTAL ORDERS
// ==========================

public long countOrders() {

    return ordersRepository.count();

}

// ==========================
// TOTAL REVENUE
// ==========================

public java.math.BigDecimal getTotalRevenue() {

    Double revenue = ordersRepository.getTotalRevenue();

    return java.math.BigDecimal.valueOf(revenue == null ? 0.0 : revenue);

}

// ==========================
// TODAY SALES
// ==========================

public java.math.BigDecimal getTodaySales(java.time.LocalDate date) {

    Double sales = ordersRepository.getTodaySales(date);

    return java.math.BigDecimal.valueOf(sales == null ? 0.0 : sales);

}

// ==========================
// MONTHLY SALES
// ==========================

public java.math.BigDecimal getMonthlySales(int month, int year) {

    Double sales = ordersRepository.getMonthlySales(month, year);

    return java.math.BigDecimal.valueOf(sales == null ? 0.0 : sales);

}

// ==========================
// YEARLY SALES
// ==========================

public java.math.BigDecimal getYearlySales(int year) {

    Double sales = ordersRepository.getYearlySales(year);

    return java.math.BigDecimal.valueOf(sales == null ? 0.0 : sales);

}

// ==========================
// RECENT ORDERS
// ==========================

public List<Orders> getRecentOrders() {

    return ordersRepository.findTop10ByOrderByPlacedAtDesc();

}

}