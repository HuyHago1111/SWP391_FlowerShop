package com.flowerShop1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set; // Thêm import này

@Entity
@Table(name = "Orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @Column(name = "order_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "shipper_id")
    private Shipper shipper;

    @ManyToOne
    @JoinColumn(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    // Sửa kiểu dữ liệu từ String sang LocalDateTime
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "note", length = 255, columnDefinition = "nvarchar(255)")
    private String note;

    // Thêm mối quan hệ ngược lại với Payment
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Payment> payments;
}