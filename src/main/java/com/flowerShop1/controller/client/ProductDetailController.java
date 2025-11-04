package com.flowerShop1.controller.client;

import com.flowerShop1.entity.Product;
import com.flowerShop1.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/product-detail")
public class ProductDetailController {
    @Autowired
    private ProductService productService;
    @GetMapping("/{id}")
    public String productDetail(@PathVariable("id") int productId, Model model) {

        try {
            model.addAttribute("product", productService.getProductById(productId));

            List<Product> relatedProducts = productService.findRelatedProducts(productService.getProductById(productId).getCategory().getCategoryId(), productId, 6);
            model.addAttribute("relatedProducts", relatedProducts);
            List<Product> trendingProducts = productService.findTrendingProducts(4);
            model.addAttribute("trendingProducts", trendingProducts);

        } catch (Exception e) {
            return "redirect:/";
        }
        return "client/productdetail";
    }

}
