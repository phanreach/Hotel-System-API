package com.example.hotel_system.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Data
public class BookingRequestDTO {

    // Optional (null = guest booking)
    private Long userId;

    @NotNull(message = "Room id is required")
    private Long roomId;

    @NotNull(message = "Check-in date is required")
    @FutureOrPresent(message = "Check-in date must be today or later")
    private LocalDate checkInDate;

    @NotNull(message = "Check-out date is required")
    @Future(message = "Check-out date must be after today")
    private LocalDate checkOutDate;

    private Long nights;

    @Valid
    @NotNull(message = "Guest information is required")
    private GuestRequestDTO guest;
}
