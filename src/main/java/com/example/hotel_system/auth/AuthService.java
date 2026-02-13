package com.example.hotel_system.auth;

import com.example.hotel_system.auth.dto.*;
import com.example.hotel_system.exception.EmailAlreadyExistsException;
import com.example.hotel_system.model.User;
import com.example.hotel_system.repository.BookingRepository;
import com.example.hotel_system.security.JwtService;
import com.example.hotel_system.auth.dto.UserDto;
import com.example.hotel_system.user.UserRepository;
import com.example.hotel_system.config.JwtProperties;
import com.example.hotel_system.enumeration.EnumRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JwtProperties jwtProperties;


    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        User user = new User();
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setRole(EnumRole.USER);

        userRepository.save(user);

        return generateAuthResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return generateAuthResponse(user);
    }

    public AuthResponse refresh(RefreshRequest request) {
        String refreshToken = request.getRefreshToken();
        String email = jwtService.extractUsername(refreshToken);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new RuntimeException("Invalid refresh token");
        }

        return generateAuthResponse(user);
    }

    private AuthResponse generateAuthResponse(User user) {

        String accessToken = jwtService.generateToken(
                user.getEmail(),
                user.getRole().getAuthority()
        );
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        AuthResponse response = new AuthResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(jwtProperties.getExpiration());
        response.setUser_id(user.getId());
        response.setFirst_name(user.getFirstName());
        response.setLast_name(user.getLastName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        response.setPhone(user.getPhone());
        return response;
    }


    private UserDto toUserRequest(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().name(),
                user.getPhone()
        );
    }

}
