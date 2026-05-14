package com.cheonan.matzip.controller;

import com.cheonan.matzip.dto.request.RestaurantCreateRequest;
import com.cheonan.matzip.dto.response.ReceiptListResponse;
import com.cheonan.matzip.service.RestaurantService;
import com.cheonan.matzip.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final RestaurantService restaurantService;
    private final ReviewService reviewService;          // 🆕 추가

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

    // ── 대시보드 ──────────────────────────────────
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

    @GetMapping("/restaurants")
    public String restaurantList() {
        return "admin/restaurant-list";
    }

    // ── 맛집 등록 ─────────────────────────────────
    @PostMapping("/restaurants")
    public String createRestaurant(RestaurantCreateRequest request) {
        restaurantService.create(request);
        return "redirect:/admin";
    }

    // ── 영수증 목록 ───────────────────────────────
    @GetMapping("/receipt-list")
    public String receiptList(Model model) {
        List<ReceiptListResponse> receipts = reviewService.getReceiptList();
        List<ReceiptListResponse> processedReceipts = reviewService.getProcessedReceiptList();

        model.addAttribute("receipts", receipts);
        model.addAttribute("processedReceipts", processedReceipts);

        return "admin/receipt-list";
    }

    // ── 영수증 승인 ───────────────────────────────
    @PostMapping("/receipt-list/{id}/approve")
    public String approveReceipt(@PathVariable Long id) {
        reviewService.approveReceipt(id);
        return "redirect:/admin/receipt-list";
    }

    // ── 영수증 거절 ───────────────────────────────
    @PostMapping("/receipt-list/{id}/reject")
    public String rejectReceipt(@PathVariable Long id) {
        reviewService.rejectReceipt(id);
        return "redirect:/admin/receipt-list";
    }
}