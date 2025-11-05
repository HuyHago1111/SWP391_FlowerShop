package com.flowerShop1.service.shipper.impl;

import com.flowerShop1.entity.Shipper;
import com.flowerShop1.repository.ShipperRepository;
import com.flowerShop1.service.shipper.ShipperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShipperServiceImpl implements ShipperService {

    @Autowired
    private ShipperRepository shipperRepository;

    @Override
    public List<Shipper> getAllShippers() {
        return shipperRepository.findAll();
    }

    @Override
    public Page<Shipper> getAllShippers(Pageable pageable) {
        return shipperRepository.findAll(pageable);
    }

    @Override
    public Page<Shipper> searchShippers(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return shipperRepository.findAll(pageable);
        }

        // Giả sử bạn có trường "vehicleNumber" và "phone"
        // thì ta sẽ lọc bằng code bên dưới
        Page<Shipper> page = shipperRepository.findAll(pageable);
        List<Shipper> filtered = page.getContent().stream()
                .filter(s -> (s.getVehicleNumber() != null && s.getVehicleNumber().toLowerCase().contains(keyword.toLowerCase()))
                        || (s.getUser() != null && s.getUser().getFullName().toLowerCase().contains(keyword.toLowerCase()))
                        || (s.getPhone() != null && s.getPhone().toLowerCase().contains(keyword.toLowerCase())))
                .toList();

        return new PageImpl<>(filtered, pageable, filtered.size());
    }

    @Override
    public Optional<Shipper> getShipperById(Integer shipperId) {
        return shipperRepository.findById(shipperId);
    }

    @Override
    public Optional<Shipper> getByUserId(Integer userId) {
        return shipperRepository.findByUser_UserId(userId);
    }

    @Override
    public Shipper saveShipper(Shipper shipper) {
        return shipperRepository.save(shipper);
    }

    @Override
    public void updateStatus(Integer shipperId, String newStatus) {
        Shipper shipper = shipperRepository.findById(shipperId)
                .orElseThrow(() -> new RuntimeException("Shipper not found"));
        shipper.setStatus(newStatus);
        shipperRepository.save(shipper);
    }

    @Override
    public void deleteShipperById(Integer shipperId) {
        shipperRepository.deleteById(shipperId);
    }
}
