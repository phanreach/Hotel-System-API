package com.example.hotel_system.repository;

import com.example.hotel_system.model.Booking;
import com.example.hotel_system.model.RoomModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByStatus(String status);

    List<Booking> id(Long id);

//    List<Booking> findByStartDate(LocalDate startDate);
//
//    List<Booking> findByEndDate(LocalDate endDate);

//    List<Booking> findByStartDateBetween(LocalDate start, LocalDate end);

    boolean existsByRoomAndCheckOutDateAfterAndCheckInDateBefore(
            RoomModel room,
            LocalDate checkIn,
            LocalDate checkOut
    );
}
