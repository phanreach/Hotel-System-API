package com.example.hotel_system.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {

    private Long bookingId;

    private String bookerName; // null if guest booking
    private RoomResponse roomResponse;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    private Double totalPrice;
    private String status;

    private GuestResponseDTO guest;
    private Long nights;
}

