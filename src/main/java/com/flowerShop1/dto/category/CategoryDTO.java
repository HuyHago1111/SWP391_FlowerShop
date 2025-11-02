package com.flowerShop1.dto.category;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CategoryDTO {
    private int categoryID;
    private String categoryName;
    private int productCount = 0;
}
