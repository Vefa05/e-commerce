package com.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;  // Correct the field name to match your database

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "order_date")
    private java.sql.Timestamp orderDate; // or java.util.Date

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }


    public java.sql.Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(java.sql.Timestamp orderDate) {
        this.orderDate = orderDate;
    }
}
