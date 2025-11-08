package com.example.simple_cqrs.shared.exception;

public class CustomException extends RuntimeException{
    public CustomException(String s) {
        super(s);
    }
}
