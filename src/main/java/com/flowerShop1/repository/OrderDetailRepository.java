package com.flowerShop1.repository;

import com.flowerShop1.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Integer> {
    @Query(value = """
   SELECT  p.product_id, p.product_name,p.image_url,p.price,SUM(od.quantity) AS total_quantity,COUNT(Distinct od.order_id) AS total_orders
                               FROM OrderDetails od
                                        JOIN Products p ON od.product_id = p.product_id
                                        JOIN Orders o ON od.order_id = o.order_id
                               WHERE o.order_status = 5
                               GROUP BY p.product_id, p.product_name,p.image_url,p.price
                               ORDER BY total_quantity DESC;
""", nativeQuery = true)
    List<Object[]> findBestSellingProductsNative();

    @Query(value = """
            SELECT TOP 10
                p.product_id, p.product_name,p.image_url,p.price,
                SUM(od.quantity) AS total_sold
               
            FROM OrderDetails od
                     JOIN Orders o ON o.order_id = od.order_id
                     JOIN Products p ON p.product_id = od.product_id
            WHERE o.order_date >= DATEADD(DAY, -7, GETDATE())
            GROUP BY p.product_id, p.product_name, p.price, p.image_url
            ORDER BY total_sold DESC;
            
            """, nativeQuery = true)
    List<Object[]> findTrendingProductsNative();

    

}
