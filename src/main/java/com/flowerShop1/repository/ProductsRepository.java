package com.flowerShop1.repository;

import com.flowerShop1.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;


public interface ProductsRepository  extends JpaRepository <Product, Integer> {
    Product findByProductId(int productId);
    @Query(value = """
select p.product_id,p.product_name,p.image_url,od.quantity, od.price,o.order_date,os.status_name
from Orders o
join OrderDetails od on od.order_id = o.order_id
join Products p on p.product_id = od.product_id
join Orders_Status_enum os on os.status_id = o.order_status
where o.user_id = :userId 

""",countQuery = """
select count (*)
from Orders o
join OrderDetails od on od.order_id = o.order_id
join Products p on p.product_id = od.product_id
join Orders_Status_enum os on os.status_id = o.order_status
where o.user_id = :userId 
""", nativeQuery = true)
    Page<Object[]> findProductsOfOrderByUserId(@Param("userId") int userId, Pageable pageable);




    @Query(
            value = """
            -- Bỏ qua tất cả các lệnh DECLARE
            -- Sử dụng trực tiếp các tham số :paramName từ method

            WITH ProductSales AS (
                SELECT
                    product_id,
                    SUM(quantity) AS TotalQuantitySold
                FROM
                    dbo.OrderDetails
                GROUP BY
                    product_id
            )
            SELECT
                p.product_id,
                p.product_name,
                p.price,
                p.stock_quantity,
                p.image_url,
                c.category_name,
                s.company_name AS supplier_name,
                ISNULL(ps.TotalQuantitySold, 0) AS total_sold
            FROM
                [dbo].[Products] p
            JOIN
                [dbo].[Categories] c ON p.category_id = c.category_id
            JOIN
                [dbo].[Suppliers] s ON p.supplier_id = s.supplier_id
            LEFT JOIN
                ProductSales ps ON p.product_id = ps.product_id
            WHERE
                -- Sử dụng trực tiếp :searchName, :categoryIDs, ...
                (:searchName IS NULL OR :searchName = '' OR p.product_name LIKE CONCAT('%', :searchName, '%'))
                AND (:categoryIDs IS NULL OR :categoryIDs = '' OR p.category_id IN (SELECT value FROM STRING_SPLIT(:categoryIDs, ',')))
                AND (:minPrice IS NULL OR p.price >= :minPrice)
                AND (:maxPrice IS NULL OR p.price <= :maxPrice)
                AND p.status = 'Active'
                AND p.stock_quantity > 0
            ORDER BY
                -- Sắp xếp động dựa vào tham số :sortBy
                CASE WHEN :sortBy = 'popularity' THEN ISNULL(ps.TotalQuantitySold, 0) END DESC,
                CASE WHEN :sortBy = 'price_desc' THEN p.price END DESC,
                CASE WHEN :sortBy = 'price_asc'  THEN p.price END ASC,
                CASE WHEN :sortBy = 'name_asc'   THEN p.product_name END ASC,
                CASE WHEN :sortBy = 'name_desc'  THEN p.product_name END DESC,
                p.product_id ASC
            """,
            countQuery = """
            SELECT COUNT(p.product_id)
            FROM [dbo].[Products] p
            WHERE
                (:searchName IS NULL OR :searchName = '' OR p.product_name LIKE CONCAT('%', :searchName, '%'))
                AND (:categoryIDs IS NULL OR :categoryIDs = '' OR p.category_id IN (SELECT value FROM STRING_SPLIT(:categoryIDs, ',')))
                AND (:minPrice IS NULL OR p.price >= :minPrice)
                AND (:maxPrice IS NULL OR p.price <= :maxPrice)
                AND p.status = 'Active'
                AND p.stock_quantity > 0
            """,
            nativeQuery = true
    )
    Page<Object[]> findProductsByManyFields(
            @Param("searchName") String searchName,
            @Param("categoryIDs") String categoryIDs,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("sortBy") String sortBy,
            Pageable pageable
    );
}
