package com.example.clinic.handler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.clinic.exception.EntityNotFoundException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Slf4j
public class AppHandlerException {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.NOT_FOUND, "Entity Not Found", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error(ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "An unexpected error occurred");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NoResourceFoundException ex) {
        log.error(ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.NOT_FOUND, "Resource not Found", "Resource not found");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.error(ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.BAD_REQUEST, "Bad Request", "Invalid parameter type");
    }

    private ResponseEntity<Map<String, Object>> buildResponseEntity(HttpStatus status, String error, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        body.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(body, status);
    }
}
