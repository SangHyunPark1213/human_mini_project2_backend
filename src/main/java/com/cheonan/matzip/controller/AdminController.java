package com.cheonan.matzip.controller;

import com.cheonan.matzip.dto.request.RestaurantCreateRequest;
import com.cheonan.matzip.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final RestaurantService restaurantService;

    @Value("${kakao.map.key}")
    private String kakaoMapKey;

    @Value("${firebase.api-key}")
    private String firebaseApiKey;

    @Value("${firebase.auth-domain}")
    private String firebaseAuthDomain;

    @Value("${firebase.project-id}")
    private String firebaseProjectId;

    @Value("${firebase.storage-bucket}")
    private String firebaseStorageBucket;

    @Value("${firebase.messaging-sender-id}")
    private String firebaseMessagingSenderId;

    @Value("${firebase.app-id}")
    private String firebaseAppId;

    @GetMapping
    public String adminHome(Model model) {
        model.addAttribute("kakaoMapKey", kakaoMapKey);

        model.addAttribute("firebaseApiKey", firebaseApiKey);
        model.addAttribute("firebaseAuthDomain", firebaseAuthDomain);
        model.addAttribute("firebaseProjectId", firebaseProjectId);
        model.addAttribute("firebaseStorageBucket", firebaseStorageBucket);
        model.addAttribute("firebaseMessagingSenderId", firebaseMessagingSenderId);
        model.addAttribute("firebaseAppId", firebaseAppId);

        return "admin/index";
    }

    @GetMapping("/receipts")
    public String receiptList(Model model) {
        model.addAttribute("receipts", Collections.emptyList());
        return "admin/receipt-list";
    }

    @PostMapping("/restaurants")
    public String createRestaurant(RestaurantCreateRequest request) {
        restaurantService.create(request);
        return "redirect:/admin";
    }
}