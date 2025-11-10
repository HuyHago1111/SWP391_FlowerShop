package com.flowerShop1.service.supplier.impl;

import com.flowerShop1.entity.Supplier;
import com.flowerShop1.repository.SuppliersRepository;
import com.flowerShop1.service.supplier.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SuppliersRepository supplierRepository;

    @Override
    public Page<Supplier> listSuppliers(String keyword, String status, Pageable pageable) {
        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasStatus = status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("all");

        if (hasKeyword && hasStatus) {
            return supplierRepository.findByCompanyNameContainingIgnoreCaseAndStatusIgnoreCase(keyword.trim(), status.trim(), pageable);
        } else if (hasKeyword) {
            return supplierRepository.findByCompanyNameContainingIgnoreCase(keyword.trim(), pageable);
        } else if (hasStatus) {
            return supplierRepository.findByStatusIgnoreCase(status.trim(), pageable);
        } else {
            return supplierRepository.findAll(pageable);
        }
    }

    @Override
    public Optional<Supplier> getById(Integer id) {
        return supplierRepository.findById(id);
    }

    @Override
    public Supplier save(Supplier s) {
        if (s.getSupplierId() == null) {
            s.setCreatedAt(LocalDateTime.now());
            if (s.getStatus() == null) s.setStatus("pending");
        }
        return supplierRepository.save(s);
    }

    @Override
    public void updateStatus(Integer id, String newStatus) {
        Supplier s = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        String current = s.getStatus().toLowerCase();
        newStatus = newStatus.toLowerCase();

        boolean valid = false;

        switch (current) {
            case "pending":
                if (newStatus.equals("approved") || newStatus.equals("rejected")) {
                    valid = true;
                }
                break;
            case "approved":
                if (newStatus.equals("rejected")) {
                    valid = true;
                }
                break;
            case "rejected":
                if (newStatus.equals("approved")) {
                    valid = true;
                }
                break;
        }

        if (!valid) {
            throw new IllegalArgumentException("❌ Invalid status transition from " + current + " → " + newStatus);
        }

        s.setStatus(newStatus);
        supplierRepository.save(s);
    }

}
