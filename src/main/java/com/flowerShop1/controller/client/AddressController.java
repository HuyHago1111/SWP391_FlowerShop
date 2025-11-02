package com.flowerShop1.controller.client;

import com.flowerShop1.dto.address.AddressDTO;
import com.flowerShop1.entity.Address;
import com.flowerShop1.mapper.address.AddressMapper;
import com.flowerShop1.service.address.AddressService;
import com.flowerShop1.service.sercurity.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AddressController {
    @Autowired
    private AddressService addressService;
    @Autowired
    private AddressMapper addressMapper;

    @PostMapping("/address")
    @ResponseBody
    public Map<String, Object> addAddress(@Valid @RequestBody AddressDTO addressDTO, BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Map<String, Object> response = new java.util.HashMap<>();
        boolean isExists = addressService.existsByUserIdAndAddressDetailAndDistrictAndPhoneAndFullName(
                customUserDetails.getUserId(),
                addressDTO.getAddressDetail(),
                addressDTO.getDistrict(),
                addressDTO.getPhone(),
                addressDTO.getFullName()
        );

        if (isExists) {
            response.put("status", "error");
            response.put("message", "Address already exists");
            return response;
        }


        if (bindingResult.hasErrors()) {
            response.put("status", "error");
            response.put("message", "Validation errors");
            Map<String, String> errors = new java.util.HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            response.put("errors", errors);
            return response;
        }
        try {
            addressDTO.setUserId(customUserDetails.getUserId());
            Address address = addressMapper.DTOtoEntity(addressDTO);
            address.setCreatedAt(LocalDateTime.now());
            if (!addressService.existsByUserId(customUserDetails.getUserId())) {
                address.setDefault(true);

            } else {
                address.setDefault(false);
            }
            addressService.save(address);


        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to add address: " + e.getMessage());
            return response;
        }
        response.put("status", "success");
        response.put("message", "Address added successfully");
        response.put("data", addressDTO);
        return response;

    }

    @GetMapping("/addresses")
    @ResponseBody
    public Map<String, Object> getAddress(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Map<String, Object> response = new java.util.HashMap<>();
        List<Address> addresses = addressService.getAddressesByUserId(customUserDetails.getUserId());
        response.put("status", "success");
        response.put("data", addresses.stream().map(addressMapper::entityToDTO).toList());

        return response;

    }

    @DeleteMapping("/address/delete")
    @ResponseBody
    public Map<String, Object> deleteAddress(@RequestParam("id") int id) {
        Map<String, Object> response = new java.util.HashMap<>();
        try {
            addressService.deleteAddress(id);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to delete address: " + e.getMessage());
            return response;
        }
        response.put("status", "success");
        response.put("message", "Address deleted successfully");
        return response;
    }

    @PostMapping("/address/set-default")
    @ResponseBody
    public Map<String, Object> setDefaultAddress(@RequestParam("id") int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            addressService.setDefaultAddress(id);


        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to set default address: " + e.getMessage());
            return response;


        }
        response.put("status", "success");
        response.put("message", "Default address set successfully");
        return response;
    }
}

