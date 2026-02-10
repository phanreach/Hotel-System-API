package com.example.hotel_system.model;

import com.example.hotel_system.enumeration.BookingStatus;
import com.example.hotel_system.model.User;
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

    // Booker (can be null for guest booking)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User booker;

    // Guest (always required)
    @ManyToOne
    @JoinColumn(name = "guest_id", nullable = false)
    private Guest guest;

    // Room
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private RoomModel room;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    private Long nights;


    // Payment
//    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
//    private Payment payment;
}

