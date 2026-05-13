package com.cheonan.matzip.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    @GetMapping
    public String adminHome() {
        return "admin/index";
    }

    @GetMapping("/login")
    public String adminLoginPage() {
        return "admin/login";
    }

    @GetMapping("/receipts")
    public String receiptList() {
        return "admin/receipt-list";
    }

    @GetMapping("/restaurants")
    public String restaurantList() {
        return "admin/restaurant-list";
    }



}

