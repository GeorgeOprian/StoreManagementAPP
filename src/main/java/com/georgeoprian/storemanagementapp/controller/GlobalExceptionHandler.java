package com.georgeoprian.storemanagementapp.controller;

import com.georgeoprian.storemanagementapp.exception.ApiException;
import com.georgeoprian.storemanagementapp.exception.BadRequestException;
import com.georgeoprian.storemanagementapp.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> handleAPIException(ApiException ex) {

        log.debug("An exception was generated");

        if (ex instanceof NotFoundException) {
            log.error("Not Found: {}", ex.getMessage());
        } else if (ex instanceof BadRequestException) {
            log.error("Bad Request: {}", ex.getMessage());
        } else {
            log.error("Unrecognized ApiException: {}", ex.getMessage());
        }


        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", ex.getStatus());
        body.put("message", ex.getMessage());
        body.put("internalCode", ex.getCode());

        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex, HttpServletRequest request) {

        log.error("Unexpected error: ", ex);

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
        body.put("message", ex.getMessage());
        body.put("URI", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

}
