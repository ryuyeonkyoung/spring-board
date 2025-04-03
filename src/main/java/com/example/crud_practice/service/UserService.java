package com.example.crud_practice.service;


import com.example.crud_practice.dto.UserDTO;
import com.example.crud_practice.exception.UserSaveException;
import lombok.RequiredArgsConstructor;
import com.example.crud_practice.entity.User;
import com.example.crud_practice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(UserDTO dto) {
        // 예외처리 : throw (optional을 반환하지 않음)
        if (dto.getEmail() == null || dto.getPassword() == null) {
            log.error("회원가입 실패: 이메일과 비밀번호는 null이 될 수 없음");
            throw new UserSaveException("Email and password must not be null");
        }

        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build()).getId();
    }
}
