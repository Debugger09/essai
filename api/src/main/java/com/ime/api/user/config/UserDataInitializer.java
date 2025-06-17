package com.ime.api.user.config;

import com.ime.api.user.model.User;
import com.ime.api.user.model.enums.ROLE;
import com.ime.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class UserDataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initUserData() {
        return args -> {
            if (userRepository.count() == 0) {
                User user = User.builder()
                        .firstName("Admin")
                        .lastName("User")
                        .email("admin@ime.com")
                        .password(passwordEncoder.encode("admin123"))
                        .role(ROLE.ADMIN)
                        .status(true)
                        .build();
                userRepository.save(user);
                System.out.println("Utilisateur admin créé : admin@ime.com / admin123");
            }
        };
    }
}