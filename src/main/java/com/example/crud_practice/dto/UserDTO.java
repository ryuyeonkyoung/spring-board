package com.example.crud_practice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/*
 * 값만 가지는 객체에는 record를 사용할 수도 있다.
 * 하지만 UserDTO는 이후 기능을 확장할 예정이니 사용하지 않을 것이다.
 * 값을 담는 용도인 Money, Temperature, Latitude, Email등의 Value Object에는 record를 사용할 수도 있다.
 * */
@Getter
@RequiredArgsConstructor
public class UserDTO {
    private final String email;
    private final String password;
}
