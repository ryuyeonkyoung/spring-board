package com.example.crud_practice.service;

import lombok.RequiredArgsConstructor;
import com.example.crud_practice.entity.User;
import com.example.crud_practice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public User loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", email);
                    return new IllegalArgumentException((email));
                }); // Optional(findByEmail) + 예외 던지기(IllegalArgumentException)
    }
}
