package com.example.hotel_system.repository;

import com.example.hotel_system.enumeration.BookingStatus;
import com.example.hotel_system.model.Booking;
import com.example.hotel_system.model.RoomModel;
import com.example.hotel_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBooker(User booker);

    boolean existsByRoomAndStatusAndCheckOutDateAfterAndCheckInDateBefore(
            RoomModel room,
            BookingStatus status,
            LocalDate checkIn,
            LocalDate checkOut
    );

    @Modifying
    @Query("""
        UPDATE Booking b
        SET b.status = 'COMPLETED'
        WHERE b.status = 'CONFIRMED'
        AND b.checkOutDate < :today
    """)
    void markCompleted(@Param("today") LocalDate today);

}
