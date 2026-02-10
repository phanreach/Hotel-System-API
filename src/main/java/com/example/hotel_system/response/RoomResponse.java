package com.example.hotel_system.response;

import com.example.hotel_system.model.Amenities;
import com.example.hotel_system.model.RoomImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    private Long id;
    private String title;
    private String description;
    private Set<AmentiesResponse> amenities = new HashSet<>();
    private double pricePerNight;
    private String roomType;
    private Integer bedSize;
    private String bedType;
    private Double rating;
    private Integer maxGuest;
    private List<String> images;
}
