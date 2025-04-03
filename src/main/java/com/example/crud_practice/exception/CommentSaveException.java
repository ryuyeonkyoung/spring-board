package com.example.crud_practice.exception;

public class CommentSaveException extends RuntimeException {
    public CommentSaveException(String message) {
        super(message);
    }
}