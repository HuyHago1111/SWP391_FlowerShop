package com.flowerShop1.repository;

import com.flowerShop1.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


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
}
