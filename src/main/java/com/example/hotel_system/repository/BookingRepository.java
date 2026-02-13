package com.example.hotel_system.repository;

import com.example.hotel_system.model.Booking;
import com.example.hotel_system.model.RoomModel;
import com.example.hotel_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBooker(User booker);

    boolean existsByRoomAndCheckOutDateAfterAndCheckInDateBefore(
            RoomModel room,
            LocalDate checkIn,
            LocalDate checkOut
    );

}
