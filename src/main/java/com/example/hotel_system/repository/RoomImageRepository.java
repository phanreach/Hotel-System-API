package com.example.hotel_system.repository;

import com.example.hotel_system.model.RoomImage;
import com.example.hotel_system.model.RoomModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomImageRepository extends JpaRepository<RoomImage, Long> {
    List<RoomImage> findByRoomId(Long roomId);
    List<RoomImage> findByRoom(RoomModel room);

    void deleteByRoomAndImageUrlNotIn(RoomModel room, List<String> imageUrls);
}
