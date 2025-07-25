package com.fashionstore.fashionstore.entity;

public enum OrderStatus {
    PENDING,      // Khách vừa đặt
    CONFIRMED,    // Shop xác nhận
    PACKED,       // Đã đóng gói
    SHIPPED,      // Giao cho đơn vị vận chuyển
    COMPLETED,    // Giao thành công
    CANCELED      // Đã huỷ
}
