package com.example.hotel_system.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final String UPLOAD_DIR = "uploads/rooms";

    public String save(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            Path uploadPath = Paths.get(UPLOAD_DIR);
            Files.createDirectories(uploadPath);

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            // stored as relative path (IMPORTANT)
            return "/uploads/rooms/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    // ✅ ADD THIS METHOD
    public void delete(String relativePath) {
        try {
            if (relativePath == null || relativePath.isBlank()) return;

            // remove leading "/"
            String cleanPath = relativePath.startsWith("/")
                    ? relativePath.substring(1)
                    : relativePath;

            Path filePath = Paths.get(cleanPath);

            Files.deleteIfExists(filePath);

        } catch (IOException e) {
            // log only, never crash transaction
            System.err.println("Failed to delete file: " + relativePath);
        }
    }
}
