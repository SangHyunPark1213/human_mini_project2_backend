package com.cheonan.matzip.controller;

import com.cheonan.matzip.dto.request.RestaurantCreateRequest;
import com.cheonan.matzip.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final RestaurantService restaurantService;

    @GetMapping
    public String adminHome() {
        return "admin/index";
    }

    @GetMapping("/receipts")
    public String receiptList() {
        return "admin/receipt-list";
    }

    @GetMapping("/restaurants/new")
    public String restaurantForm(Model model) {
        model.addAttribute("restaurant", new RestaurantCreateRequest());
        return "admin/restaurant-form";
    }

    @PostMapping("/restaurants")
    public String createRestaurant(RestaurantCreateRequest request) {
        restaurantService.create(request);
        return "redirect:/admin";
    }
}