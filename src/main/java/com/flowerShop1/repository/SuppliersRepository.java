package com.flowerShop1.repository;

import com.flowerShop1.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuppliersRepository extends JpaRepository<Supplier, Integer> {
    Page<Supplier> findByCompanyNameContainingIgnoreCase(String companyName, Pageable pageable);
    Page<Supplier> findByStatusIgnoreCase(String status, Pageable pageable);
    Page<Supplier> findByCompanyNameContainingIgnoreCaseAndStatusIgnoreCase(String companyName, String status, Pageable pageable);

}
