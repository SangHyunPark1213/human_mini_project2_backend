package com.cheonan.matzip.service;

import com.cheonan.matzip.dao.RestaurantDao;
import com.cheonan.matzip.dto.Restaurant;
import com.cheonan.matzip.dto.request.RestaurantCreateRequest;
import com.cheonan.matzip.dto.response.RestaurantResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantDao restaurantDao;

    public Long create(RestaurantCreateRequest request) {
        return restaurantDao.save(request);
    }

    public List<RestaurantResponse> findAll(String category) {
        return restaurantDao.findAll(category)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public RestaurantResponse findById(Long id) {
        Restaurant restaurant = restaurantDao.findById(id)
                .orElseThrow(() -> new RuntimeException("맛집을 찾을 수 없습니다."));

        return toResponse(restaurant);
    }

    private RestaurantResponse toResponse(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getPhone(),
                restaurant.getCategory(),
                restaurant.getLatitude(),
                restaurant.getLongitude(),
                restaurant.getAverageRating(),
                restaurant.getReviewCount(),
                restaurant.getThumbnail(),
                restaurant.getPopularMenu()
        );
    }

    public void update(Long id, RestaurantCreateRequest request) {
        // 존재 여부 먼저 확인
        restaurantDao.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("존재하지 않는 맛집입니다.")
                );
        restaurantDao.update(id, request);
    }

    public void delete(Long id) {
        // 존재 여부 먼저 확인
        restaurantDao.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("존재하지 않는 맛집입니다.")
                );
        restaurantDao.delete(id);
    }
}