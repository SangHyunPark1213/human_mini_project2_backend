package com.cheonan.matzip.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestaurantResponse {

    private Long id;
    private String name;
    private String address;
    private String phone;
    private String category;
    private Double latitude;
    private Double longitude;
    private Double averageRating;
    private Integer reviewCount;
    private String thumbnail;
    private String popularMenu;
}