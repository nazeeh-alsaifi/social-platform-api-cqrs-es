package com.example.simple_cqrs.shared;

import com.example.simple_cqrs.shared.exception.CustomException;
import com.example.simple_cqrs.shared.exception.PostNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "INVALID_INPUT", String.valueOf(errors));
    }

//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
//        return buildErrorResponse(HttpStatus.BAD_REQUEST, "INVALID_INPUT", ex.getMessage());
//    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePostNotFound(PostNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "NOT_FOUND",
                ex.getMessage());
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, String>> handleCustomException(CustomException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "BAD_REQUEST",
                ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneral(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR",
                ex.getMessage());
    }

    private ResponseEntity<Map<String, String>> buildErrorResponse(
            HttpStatus status, String code, String message) {
        Map<String, String> error = new HashMap<>();
        error.put("timestamp", String.valueOf(Instant.now()));
        error.put("status", String.valueOf(status.value()));
        error.put("code", code);
        error.put("message", message);
        return ResponseEntity.status(status).body(error);
    }
}
