package com.example.hotel_system.controller;

import com.example.hotel_system.model.RoomImage;
import com.example.hotel_system.model.RoomModel;
import com.example.hotel_system.request.RoomRequest;
import com.example.hotel_system.response.RoomDetailsResponse;
import com.example.hotel_system.response.RoomResponse;
import com.example.hotel_system.service.RoomService;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public Page<RoomResponse> getRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        return roomService.getRooms(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDetailsResponse> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> createRoom(@RequestBody RoomRequest request) {
        RoomModel room = roomService.createRoom(request);
        Map<String, Object> res = new HashMap<>();
        res.put("id", room.getId());
        return ResponseEntity.ok(res);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomModel> updateRoom(
            @PathVariable Long id,
            @RequestBody RoomRequest request
    ) {
        return ResponseEntity.ok(roomService.updateRoom(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(
            value = "/upload-images/{roomId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoomImage>> uploadImages(
            @PathVariable Long roomId,
            @RequestParam("images") MultipartFile[] files
    ) {
        return ResponseEntity.ok(
                roomService.uploadRoomImages(roomId, Arrays.asList(files))
        );
    }

    @PutMapping(
            value = "/{roomId}/update-images",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoomImage>> updateRoomImages(
            @PathVariable Long roomId,
            @RequestParam("images") MultipartFile[] files
    ) {
        return ResponseEntity.ok(
                roomService.updateRoomImages(roomId, Arrays.asList(files))
        );
    }

    @PutMapping("/{roomId}/sync-images")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> syncRoomImages(
            @PathVariable("roomId") Long roomId,
            @RequestBody List<String> keepImages
    ) {
        roomService.syncRoomImages(roomId, keepImages);
        return ResponseEntity.ok().build();
    }

}

