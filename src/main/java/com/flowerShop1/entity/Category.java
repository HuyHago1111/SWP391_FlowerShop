package com.flowerShop1.entity;
//    category_id   int identity
//        primary key,
//    category_name nvarchar(100) not null,
//    description   nvarchar(255)
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Category {
    @Id
    @Column (name = "category_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    @Column(name = "category_name", nullable = false, length = 100, columnDefinition = "nvarchar(100)")
    private String categoryName;
    @Column(name = "description", length = 255, columnDefinition = "nvarchar(255)" )
    private String description;
}
