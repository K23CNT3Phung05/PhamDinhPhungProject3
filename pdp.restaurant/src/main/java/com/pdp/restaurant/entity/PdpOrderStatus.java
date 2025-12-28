package com.pdp.restaurant.entity;

public enum PdpOrderStatus {
    PENDING,
    CONFIRMED,   // Đảm bảo có dòng này
    SHIPPING,
    COMPLETED,
    CANCELLED,
    PAID
}