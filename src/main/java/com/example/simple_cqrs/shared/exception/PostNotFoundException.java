package com.example.simple_cqrs.shared.exception;


public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(String s) {
        super(s);
    }
}
