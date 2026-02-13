package com.example.hotel_system.config;

import com.example.hotel_system.enumeration.EnumRole;
import com.example.hotel_system.model.User;
import com.example.hotel_system.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository) {
        return args -> {
            if (!userRepository.existsByEmail("admin@gmail.com")) {
                User admin = new User();
                admin.setEmail("admin@gmail.com");
                admin.setFirstName("System");
                admin.setLastName("Admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setPhone("010203040");
                admin.setRole(EnumRole.ADMIN);
                userRepository.save(admin);
            }
        };
    }
}
