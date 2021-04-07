package com.example.exception.custom;

public class CustomException extends RuntimeException{
    public CustomException(String message) {
        super(message);
    }
}
