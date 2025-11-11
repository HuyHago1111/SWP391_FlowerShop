package com.flowerShop1.controller.client;

import com.flowerShop1.service.category.CategoryService;
import com.flowerShop1.service.product.ProductService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List; // ✅ SỬA LỖI: Cần import List
import java.util.Map;

@Controller
public class FlowerListController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    // ✅ SỬA LỖI: Đặt hằng số giới hạn
    private static final int MAX_PAGE_SIZE = 50;
    private static final int DEFAULT_PAGE_SIZE = 8;
    private static final String DEFAULT_SORT = "popularity";

    @GetMapping("/flower-list")
    @ResponseBody
    public Map<String, Object> flowerList(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "8", name = "size") int size,
            @RequestParam(defaultValue = DEFAULT_SORT, name = "sortBy") String sortBy, // ✅ SỬA LỖI: Dùng hằng số
            @RequestParam(defaultValue = "", name = "searchName") String searchName,
            @RequestParam(defaultValue = "", name = "categoryIDs") String categoryIDs,
            @RequestParam(defaultValue = "", name = "minPrice") String minPrice,
            @RequestParam(defaultValue = "", name = "maxPrice") String maxPrice) {

        Map<String, Object> response = new java.util.HashMap<>();

        // ✅ SỬA LỖI 1: Validate phân trang chống DoS
        if (page < 0) {
            page = 0;
        }
        if (size > MAX_PAGE_SIZE || size <= 0) {
            size = DEFAULT_PAGE_SIZE;
        }


        List<String> allowedSorts = List.of("popularity", "priceAsc", "priceDesc", "name");
        if (!allowedSorts.contains(sortBy)) {
            sortBy = DEFAULT_SORT;
        }

        Pageable pageable = PageRequest.of(page, size);
        BigDecimal minPriceObj = null;
        BigDecimal maxPriceObj = null;


        try {
            if (minPrice != null && !minPrice.isEmpty()) {
                minPriceObj = new BigDecimal(minPrice);
            }
            if (maxPrice != null && !maxPrice.isEmpty()) {
                maxPriceObj = new BigDecimal(maxPrice);
            }
        } catch (NumberFormatException e) {

            response.put("status", "error");
            response.put("message", "Giá trị minPrice hoặc maxPrice không hợp lệ.");
            // Bạn nên trả về mã lỗi 400 Bad Request ở đây
            return response;
        }

        response.put("data", productService.getProductsByManyFields(searchName, categoryIDs, minPriceObj, maxPriceObj, sortBy, pageable));
        response.put("status", "success");
        return response;
    }

    @GetMapping("/flower")
    public String getFlowerList(Model model, @RequestParam(name = "categoryIDs", required = false, defaultValue = "") String categoryIDs) {
        System.out.println("categoryIDs" + categoryIDs);
        model.addAttribute("categoriesDTO", categoryService.getAllCategoriesWithProductCount());


        model.addAttribute("initialCategoryIDs", categoryIDs);

        return "client/flowerList";
    }
}