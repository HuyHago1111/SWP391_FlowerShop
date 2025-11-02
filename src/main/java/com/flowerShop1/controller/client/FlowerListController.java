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
import java.util.Map;

@Controller
public class FlowerListController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/flower-list")
    @ResponseBody
    public Map<String, Object> flowerList( @RequestParam(defaultValue = "0", name = "page") int page,
                                           @RequestParam(defaultValue = "8", name = "size") int size ,@RequestParam(defaultValue = "popularity", name = "sortBy") String sortBy,
                                           @RequestParam(defaultValue = "", name = "searchName") String searchName,
                                           @RequestParam(defaultValue = "", name = "categoryIDs") String categoryIDs,
                                           @RequestParam(defaultValue = "", name = "minPrice") String minPrice,
                                           @RequestParam(defaultValue = "", name = "maxPrice") String maxPrice) {
        Map<String, Object> response = new java.util.HashMap<>();
        Pageable pageable = PageRequest.of(page, size);
        BigDecimal minPriceObj =null;
        BigDecimal maxPriceObj = null;
        if (!minPrice.isEmpty()) {
            minPriceObj = new BigDecimal(minPrice);
        }
        if (!maxPrice.isEmpty()) {
            maxPriceObj = new BigDecimal(maxPrice);
        }

        response.put("data", productService.getProductsByManyFields(searchName, categoryIDs, minPriceObj, maxPriceObj, sortBy, pageable));
        response.put("status", "success");
        return response;


    }
   @GetMapping("/flower")
    public String getFlowerList(Model model, @RequestParam(name = "categoryIDs", required = false, defaultValue = "") String categoryIDs){
       System.out.println("categoryIDs"+ categoryIDs);
        model.addAttribute("categoriesDTO", categoryService.getAllCategoriesWithProductCount());
       model.addAttribute("initialCategoryIDs", categoryIDs);

        return "client/flowerList";
   }



}
