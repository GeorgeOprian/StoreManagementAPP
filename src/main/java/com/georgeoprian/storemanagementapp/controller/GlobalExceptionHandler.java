package com.georgeoprian.storemanagementapp.controller;

import com.georgeoprian.storemanagementapp.exception.ApiException;
import com.georgeoprian.storemanagementapp.exception.BadRequestException;
import com.georgeoprian.storemanagementapp.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> handleAPIException(ApiException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", ex.getStatus());
        body.put("message", ex.getMessage());

        return ResponseEntity.status(ex.getStatus()).body(body);
    }

}
