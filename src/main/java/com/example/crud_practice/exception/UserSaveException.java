package com.example.crud_practice.exception;

public class UserSaveException extends RuntimeException {
    public UserSaveException(String message) {
        super(message);
    }
}
