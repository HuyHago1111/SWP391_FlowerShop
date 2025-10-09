package com.flowerShop1.controller.client;

import com.flowerShop1.service.category.CategoryService;
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

    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("categories",categoryService.getAllCategory());

        return "index";
    }

}

