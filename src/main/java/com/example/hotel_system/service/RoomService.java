package com.example.hotel_system.service;

import com.example.hotel_system.model.Amenities;
import com.example.hotel_system.model.RoomImage;
import com.example.hotel_system.model.RoomModel;
import com.example.hotel_system.repository.AmenityRepository;
import com.example.hotel_system.repository.RoomImageRepository;
import com.example.hotel_system.repository.RoomRepository;
import com.example.hotel_system.request.RoomRequest;
import com.example.hotel_system.response.AmentiesResponse;
import com.example.hotel_system.response.RoomDetailsResponse;
import com.example.hotel_system.response.RoomResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomImageRepository roomImageRepository;
    private final AmenityRepository amenityRepository;
    private final FileStorageService storageService;

    @Value("${app.base-url}")
    private String baseUrl;

    public RoomService(
            RoomRepository roomRepository,
            RoomImageRepository roomImageRepository,
            AmenityRepository amenityRepository,
            FileStorageService storageService
    ) {
        this.roomRepository = roomRepository;
        this.roomImageRepository = roomImageRepository;
        this.amenityRepository = amenityRepository;
        this.storageService = storageService;
    }

    // ================= ROOM CRUD =================

    public Page<RoomResponse> getRooms(Pageable pageable) {
        return roomRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    public RoomDetailsResponse getRoomById(Long id) {
        RoomModel room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        return mapToDetailsResponse(room);
    }

    public RoomModel createRoom(RoomRequest request) {

        RoomModel room = new RoomModel();

        Set<Amenities> amenities = request.getAmenityIds()
                .stream()
                .map(id -> amenityRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Amenity not found")))
                .collect(Collectors.toSet());

        room.setTitle(request.getTitle());
        room.setDescription(request.getDescription());
        room.setRoomType(request.getRoomType());
        room.setPricePerNight(request.getPricePerNight());
        room.setBedSize(request.getBedSize());
        room.setBedType(request.getBedType());
        room.setRating(request.getRating());
        room.setMaxGuest(request.getMaxGuest());
        room.setAmenities(amenities);

        return roomRepository.save(room);
    }

    public RoomModel updateRoom(Long id, RoomRequest request) {
        RoomModel room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setTitle(request.getTitle());
        room.setDescription(request.getDescription());
        room.setRoomType(request.getRoomType());
        room.setPricePerNight(request.getPricePerNight());
        room.setBedSize(request.getBedSize());
        room.setBedType(request.getBedType());
        room.setRating(request.getRating());
        room.setMaxGuest(request.getMaxGuest());

        if (request.getAmenityIds() != null) {
            Set<Amenities> amenities = request.getAmenityIds()
                    .stream()
                    .map(aid -> amenityRepository.findById(aid)
                            .orElseThrow(() -> new RuntimeException("Amenity not found")))
                    .collect(Collectors.toSet());
            room.setAmenities(amenities);
        }

        return roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    @Transactional
    public List<RoomImage> uploadRoomImages(Long roomId, List<MultipartFile> files) {

        RoomModel room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (files == null || files.isEmpty()) {
            throw new RuntimeException("No files provided");
        }

        List<RoomImage> images = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String relativePath = storageService.save(file);

            RoomImage image = new RoomImage();
            image.setImageUrl(relativePath);   // ✅ RELATIVE
            image.setImageType(file.getContentType());
            image.setRoom(room);

            images.add(image);
        }

        return roomImageRepository.saveAll(images);
    }

    private RoomResponse mapToResponse(RoomModel room) {
        RoomResponse response = new RoomResponse();

        response.setId(room.getId());
        response.setTitle(room.getTitle());
        response.setDescription(room.getDescription());
        response.setRoomType(room.getRoomType());
        response.setPricePerNight(room.getPricePerNight());
        response.setBedSize(room.getBedSize());
        response.setBedType(room.getBedType());
        response.setRating(Double.valueOf(room.getRating()));
        response.setMaxGuest(room.getMaxGuest());

        response.setAmenities(
                room.getAmenities()
                        .stream()
                        .map(this::mapAmenity)
                        .collect(Collectors.toSet())
        );

        response.setImages(
                room.getImages()
                        .stream()
                        .map(img -> baseUrl + img)
                        .toList()
        );

        return response;
    }

    private RoomDetailsResponse mapToDetailsResponse(RoomModel room) {
        return new RoomDetailsResponse(
                room.getId(),
                room.getTitle(),
                room.getDescription(),
                room.getAmenities()
                        .stream()
                        .map(this::mapAmenity)
                        .collect(Collectors.toSet()),
                room.getPricePerNight(),
                room.getMaxGuest(),
                room.getImages()
                        .stream()
                        .map(img -> baseUrl + img)
                        .toList()
        );
    }

    private AmentiesResponse mapAmenity(Amenities amenity) {
        AmentiesResponse res = new AmentiesResponse();
        res.setId(amenity.getId());
        res.setName(amenity.getName());
        res.setIcon(baseUrl + amenity.getIcon());
        return res;
    }
}
