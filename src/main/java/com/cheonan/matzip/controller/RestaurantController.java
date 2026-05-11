package com.cheonan.matzip.controller;

import com.cheonan.matzip.dto.RestaurantCreateRequest;
import com.cheonan.matzip.dto.RestaurantResponse;
import com.cheonan.matzip.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody RestaurantCreateRequest request) {
        Long id = restaurantService.create(request);
        return ResponseEntity.ok(id);
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> findAll() {
        return ResponseEntity.ok(restaurantService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.findById(id));
    }
}