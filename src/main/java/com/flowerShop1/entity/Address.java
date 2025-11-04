package com.flowerShop1.entity;

import com.flowerShop1.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;

import java.time.LocalDateTime;
import java.util.Set;
import jakarta.persistence.CascadeType;

@Entity
@Table(name = "Addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    /*
     *
     * CREATE TABLE [dbo].[Addresses](
     * [address_id] [bigint] IDENTITY(1,1) NOT NULL,
     * [user_id] [int] NOT NULL,
     * [full_name] [nvarchar](255) NOT NULL,
     * [phone] [nvarchar](20) NOT NULL,
     * [province] [nvarchar](100) NOT NULL,
     * [district] [nvarchar](100) NOT NULL,
     * [is_default] [bit] NOT NULL,
     * [created_at] [datetime] NOT NULL,
     * [updated_at] [datetime] NOT NULL,
     * [address_detail] [nvarchar](255) NOT NULL,
     * PRIMARY KEY CLUSTERED
     * (
     * [address_id] ASC
     * )WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF,
     * ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY =
     * OFF) ON [PRIMARY]
     * ) ON [PRIMARY]
     * GO
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id", nullable = false)
    private int addressId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "full_name", nullable = false, length = 255, columnDefinition = "nvarchar(255)")
    private String fullName;
    @Column(name = "phone", nullable = false, length = 20, columnDefinition = "nvarchar(20)")
    private String phone;

    @Column(name = "district", nullable = false, length = 100, columnDefinition = "nvarchar(100)")
    private String district;
    @Column(name = "is_default", nullable = false)
    private boolean isDefault;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "address_detail", nullable = false, length = 255, columnDefinition = "nvarchar(255)")
    private String addressDetail;
    // ✅ THÊM MỐI QUAN HỆ NGƯỢC LẠI
    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Order> orders;

}