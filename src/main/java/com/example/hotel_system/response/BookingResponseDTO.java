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
    private Double totalPrice;
    private Long nights;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    private String bookerName;
    private String bookerEmail;
    private String bookerPhone;
    private String specialRequest;

    private RoomResponse roomResponse;
}



