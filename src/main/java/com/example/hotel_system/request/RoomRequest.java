package com.example.hotel_system.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class RoomRequest {
    private String title;
    private String description;
    private double pricePerNight;
    private String roomType;
    private List<Long> amenityIds;
    private String bedType;
    private Integer bedSize;
    private Float rating;
    private Integer maxGuest;
}



