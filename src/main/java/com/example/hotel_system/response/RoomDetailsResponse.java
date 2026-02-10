package com.example.hotel_system.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDetailsResponse {
    private Long id;
    private String title;
    private String description;
    private Set<AmentiesResponse> amenities = new HashSet<>(); // <-- Use DTO here
    private double pricePerNight;

    private Integer guests;
    private List<String> images;

}
