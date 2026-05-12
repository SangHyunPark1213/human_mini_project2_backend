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
    public ResponseEntity<List<RestaurantResponse>> findAll(
            @RequestParam(required = false) String category
    ) {
        return ResponseEntity.ok(restaurantService.findAll(category));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.findById(id));
    }

    // ── 수정 ─────────────────────────────────────────────
    // PUT /api/restaurants/{id}
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody RestaurantCreateRequest request) {
        restaurantService.update(id, request);
        return ResponseEntity.ok("맛집 수정 완료");
    }

    // ── 삭제 ─────────────────────────────────────────────
    // DELETE /api/restaurants/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        restaurantService.delete(id);
        return ResponseEntity.ok("맛집 삭제 완료");
    }
}