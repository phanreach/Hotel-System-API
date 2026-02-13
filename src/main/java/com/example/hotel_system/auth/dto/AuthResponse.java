package com.example.hotel_system.auth.dto;

import lombok.Data;

@Data
public class AuthResponse{
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private Long user_id;
    private String first_name;
    private String last_name;
    private String email;
    private String role;
    private String phone;
}

