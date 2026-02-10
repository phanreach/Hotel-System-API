package com.example.hotel_system.controller;

import com.example.hotel_system.model.Booking;
import com.example.hotel_system.request.BookingRequest;
import com.example.hotel_system.request.BookingRequestDTO;
import com.example.hotel_system.response.BookingResponseDTO;
import com.example.hotel_system.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService){
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<BookingResponseDTO> getAllBookings(){
        return bookingService.getAllBookings();
    }

    @GetMapping("/status/{status}")
    public List<Booking> getBookingsByStatus(@PathVariable String status) {
        return bookingService.getBookingByStatus(status);
    }

//    @GetMapping("/date/start/{date}")
//    public List<Booking> getBookingsByStartDate(
//            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//        return bookingService   .getBookingsByStartDate(date);
//    }
//
//    @GetMapping("/date/end/{date}")
//    public List<Booking> getBookingsByEndDate(
//            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//        return bookingService.getBookingsByEndDate(date);
//    }

//    @GetMapping("/date/range")
//    public List<Booking> getBookingsByDateRange(
//            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
//            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
//        return bookingService.getBookingsByDateRange(start, end);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(booking);
    }

//    @PostMapping
//    public ResponseEntity<Booking> createBooking(@Validated @RequestBody BookingRequest request){
//        Booking savedBooking = bookingService.createBooking(request);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedBooking);
//    }
@PostMapping
public ResponseEntity<BookingResponseDTO> createBooking(
        @Valid @RequestBody BookingRequestDTO request) {

    return ResponseEntity.ok(bookingService.createBooking(request));
}


}
