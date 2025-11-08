package com.example.simple_cqrs.shared.exception;

public class CustomValidationException extends RuntimeException{
    public CustomValidationException(String s) {
        super(s);
    }
}
