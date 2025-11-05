package com.flowerShop1.service.supplier;

import com.flowerShop1.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface SupplierService {
    Page<Supplier> listSuppliers(String keyword, String status, Pageable pageable);
    Optional<Supplier> getById(Integer id);
    Supplier save(Supplier s);
    void updateStatus(Integer id, String newStatus);
}
