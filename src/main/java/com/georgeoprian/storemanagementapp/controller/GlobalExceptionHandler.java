package com.georgeoprian.storemanagementapp.controller;

import com.georgeoprian.storemanagementapp.exception.ApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> handleAPIException(ApiException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", ex.getStatus());
        body.put("message", ex.getMessage());
        body.put("internalCode", ex.getCode());

        return ResponseEntity.status(ex.getStatus()).body(body);
    }

}
