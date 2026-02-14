package com.example.hotel_system.model;

import com.example.hotel_system.enumeration.BookingStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Logged-in user who booked
    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User booker;

    // Room
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private RoomModel room;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    private Double totalPrice;
    private Long nights;

    private String phone;
    private String email;
    private String specialRequest;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;


}



