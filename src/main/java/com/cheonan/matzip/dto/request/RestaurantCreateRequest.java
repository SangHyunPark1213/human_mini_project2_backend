package com.cheonan.matzip.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantCreateRequest {

    private String name;
    private String address;
    private String phone;
    private String category;
    private Double latitude;
    private Double longitude;
    private String thumbnail;
    private String popularMenu;
}