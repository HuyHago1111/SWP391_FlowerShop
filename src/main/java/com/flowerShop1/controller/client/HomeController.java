package com.flowerShop1.controller.client;

import com.flowerShop1.entity.Product;
import com.flowerShop1.service.category.CategoryService;
import com.flowerShop1.service.orderdetail.OrderDetailService;
import com.flowerShop1.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired

    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderDetailService orderDetailService;


    @GetMapping("")
    public String home(Model model) {
        System.out.println( "Top Selling Products: " + orderDetailService.getTopSelilingProducts());

        model.addAttribute("products", productService.getAllProduct().stream().filter(product -> product.getCategory().getCategoryId() == 1).limit(14).toList());
        model.addAttribute("products_2", productService.getAllProduct().stream().filter(product -> product.getCategory().getCategoryId() == 2).limit(14).toList());
        model.addAttribute("topSellingProducts",orderDetailService.getTopSelilingProducts().stream().limit(4).toList());
        model.addAttribute("trendingProducts", orderDetailService.getTrendingProducts().stream().limit(4).toList());

        return "client/index";
    }

}

