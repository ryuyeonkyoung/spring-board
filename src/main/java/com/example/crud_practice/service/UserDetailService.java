package com.example.crud_practice.service;

import lombok.RequiredArgsConstructor;
import com.example.crud_practice.entity.User;
import com.example.crud_practice.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException((email))); // Optional(findByEmail) + 예외 던지기(IllegalArgumentException)
    }
}
