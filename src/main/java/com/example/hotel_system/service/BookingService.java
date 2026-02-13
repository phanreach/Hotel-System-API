package com.example.hotel_system.service;

import com.example.hotel_system.enumeration.BookingStatus;
import com.example.hotel_system.model.Booking;
import com.example.hotel_system.model.RoomModel;
import com.example.hotel_system.model.User;
import com.example.hotel_system.repository.BookingRepository;
import com.example.hotel_system.repository.RoomRepository;
import com.example.hotel_system.request.BookingRequestDTO;
import com.example.hotel_system.response.BookingResponseDTO;
import com.example.hotel_system.response.RoomResponse;
import com.example.hotel_system.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public BookingResponseDTO createBooking(BookingRequestDTO request) {

        // 1️⃣ Validate dates
        if (!request.getCheckOutDate().isAfter(request.getCheckInDate())) {
            throw new IllegalArgumentException("Check-out must be after check-in");
        }

        // 2️⃣ Get logged-in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("User must be logged in to book a room");
        }
        String email = auth.getName();
        User booker = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3️⃣ Load room
        RoomModel room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // 4️⃣ Check availability
        boolean isAvailable = !bookingRepository.existsByRoomAndCheckOutDateAfterAndCheckInDateBefore(
                room,
                request.getCheckInDate(),
                request.getCheckOutDate()
        );
        if (!isAvailable) {
            throw new RuntimeException("Room not available for selected dates");
        }

        // 5️⃣ Calculate price
        long nights = ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate());
        double totalPrice = nights * room.getPricePerNight();

        // 6️⃣ Create booking
        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setRoom(room);
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setNights(nights);
        booking.setTotalPrice(totalPrice);
        booking.setPhone(request.getPhone());
        booking.setSpecialRequest(request.getSpecialRequest());

        bookingRepository.save(booking);

        // 7️⃣ Build response
        return mapToResponse(booking);
    }

    public BookingResponseDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElse(null); // return null if not found
        if (booking == null) return null;
        return mapToResponse(booking);
    }

    private BookingResponseDTO mapToResponse(Booking booking) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setBookingId(booking.getId());
        dto.setTotalPrice(booking.getTotalPrice());
        dto.setNights(booking.getNights());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());

        // ✅ Null-safe booker name
        if (booking.getBooker() != null) {
            dto.setBookerName(booking.getBooker().getFirstName() + " " + booking.getBooker().getLastName());
            dto.setBookerEmail(booking.getBooker().getEmail());
            dto.setBookerPhone(booking.getBooker().getPhone());
        } else {
            dto.setBookerName("Unknown");
            dto.setBookerEmail("Unknown");
        }

        RoomResponse roomResponse = new RoomResponse();
        if (booking.getRoom() != null) {
            roomResponse.setId(booking.getRoom().getId());
            roomResponse.setTitle(booking.getRoom().getTitle());
            roomResponse.setRoomType(booking.getRoom().getRoomType());
            roomResponse.setBedType(booking.getRoom().getBedType());
            roomResponse.setImages(
                    booking.getRoom().getImages() != null ?
                            booking.getRoom().getImages().stream()
                                    .map(img -> baseUrl + img.getImageUrl())
                                    .toList()
                            : List.of()
            );
        }
        dto.setRoomResponse(roomResponse);

        return dto;
    }

    public List<BookingResponseDTO> getMyBookings() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return bookingRepository.findByBooker(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

}

