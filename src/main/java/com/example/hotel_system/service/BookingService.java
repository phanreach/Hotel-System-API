package com.example.hotel_system.service;

import com.example.hotel_system.enumeration.BookingStatus;
import com.example.hotel_system.model.Booking;
import com.example.hotel_system.model.Guest;
import com.example.hotel_system.model.RoomModel;
import com.example.hotel_system.model.User;
import com.example.hotel_system.repository.BookingRepository;
import com.example.hotel_system.repository.GuestRepository;
import com.example.hotel_system.repository.RoomRepository;
import com.example.hotel_system.request.BookingRequest;
import com.example.hotel_system.request.BookingRequestDTO;
import com.example.hotel_system.response.BookingResponseDTO;
import com.example.hotel_system.response.GuestResponseDTO;
import com.example.hotel_system.response.RoomResponse;
import com.example.hotel_system.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final GuestRepository guestRepository;


    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository, UserRepository userRepository,GuestRepository guestRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.guestRepository = guestRepository;
    }
    @Value("${app.base-url}")
    private String baseUrl;

    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    public List<Booking> getBookingByStatus(String status) {
        return bookingRepository.findByStatus(status);
    }
//    public List<Booking> getBookingsByStartDate(LocalDate startDate) {
//        return bookingRepository.findByStartDate(startDate);
//    }

//    public List<Booking> getBookingsByEndDate(LocalDate endDate) {
//        return bookingRepository.findByEndDate(endDate);
//    }

//    public List<Booking> getBookingsByDateRange(LocalDate start, LocalDate end) {
//        return bookingRepository.findByStartDateBetween(start, end);
//    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public BookingResponseDTO createBooking(BookingRequestDTO request) {

        // 1️⃣ Validate date range
        if (!request.getCheckOutDate().isAfter(request.getCheckInDate())) {
            throw new IllegalArgumentException("Check-out must be after check-in");
        }

        // 2️⃣ Load optional user
        User booker = null;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();


        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {

            String email = auth.getName();

            booker = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }


        // 3️⃣ Load room
        RoomModel room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // 4️⃣ Check availability
        boolean isAvailable = bookingRepository
                .existsByRoomAndCheckOutDateAfterAndCheckInDateBefore(
                        room,
                        request.getCheckInDate(),
                        request.getCheckOutDate()
                );

//        if (isAvailable) {
//            throw new RuntimeException("Room not available for selected dates");
//        }

        // 5️⃣ Save guest
        Guest guest = new Guest();
        guest.setFirstName(request.getGuest().getFirstName());
        guest.setLastName(request.getGuest().getLastName());
        guest.setEmail(
                request.getGuest().getEmail().trim().toLowerCase()
        );
        guest.setPhone(request.getGuest().getPhone());

        guestRepository.save(guest);

        // 6️⃣ Calculate price
        long nights = ChronoUnit.DAYS.between(
                request.getCheckInDate(),
                request.getCheckOutDate()
        );
        double totalPrice = nights * room.getPricePerNight();

        // 7️⃣ Create booking
        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setGuest(guest);
        booking.setRoom(room);
        booking.setNights(nights);
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.PENDING);

        bookingRepository.save(booking);

        // 8️⃣ Build response
        return mapToResponse(booking);
    }
    public List<BookingResponseDTO> getMyBookings() {

        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return bookingRepository.findByBooker(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private BookingResponseDTO mapToResponse(Booking booking) {


        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setBookingId(booking.getId());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        dto.setTotalPrice(booking.getTotalPrice());
        dto.setStatus(booking.getStatus().name());
        dto.setNights(booking.getNights());

        if (booking.getBooker() != null) {
            dto.setBookerName(booking.getBooker().getLastName());
        }
        RoomResponse roomResponse = new RoomResponse();
        roomResponse.setId(booking.getRoom().getId());
        roomResponse.setTitle(booking.getRoom().getTitle());
        roomResponse.setImages(
                booking.getRoom()
                        .getImages()
                        .stream()
                        .map(img -> baseUrl + img.getImageUrl())
                        .toList()
        );
        roomResponse.setRoomType(booking.getRoom().getRoomType());
        roomResponse.setBedType(booking.getRoom().getBedType());
        dto.setRoomResponse(roomResponse);

//        roomResponse



        GuestResponseDTO guestDTO = new GuestResponseDTO();
        guestDTO.setId(booking.getGuest().getId());
        guestDTO.setFirstName(booking.getGuest().getFirstName());
        guestDTO.setLastName(booking.getGuest().getLastName());
        guestDTO.setEmail(booking.getGuest().getEmail());
        guestDTO.setPhone(booking.getGuest().getPhone());

        dto.setGuest(guestDTO);


        return dto;
    }
}
