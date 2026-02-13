package com.example.hotel_system.controller;

import com.example.hotel_system.model.Booking;
import com.example.hotel_system.request.BookingRequestDTO;
import com.example.hotel_system.response.BookingResponseDTO;
import com.example.hotel_system.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Get all bookings (admin use only)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<BookingResponseDTO> getAllBookings() {
        return bookingService.getAllBookings();
    }

    /**
     * Get all bookings of the logged-in user
     */
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public List<BookingResponseDTO> getMyBookings() {
        return bookingService.getMyBookings();
    }

    /**
     * Get booking by ID
     * Admin can access any booking
     * Users can only access their own bookings
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Long id) {
        BookingResponseDTO booking = bookingService.getBookingById(id);
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(booking);
    }

    /**
     * Create a booking for logged-in user
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookingResponseDTO> createBooking(
            @Valid @RequestBody BookingRequestDTO request) {
        BookingResponseDTO bookingResponse = bookingService.createBooking(request);
        return ResponseEntity.ok(bookingResponse);
    }

}
