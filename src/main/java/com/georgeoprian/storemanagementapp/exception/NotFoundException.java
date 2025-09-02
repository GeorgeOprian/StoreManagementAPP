package com.georgeoprian.storemanagementapp.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {

    public NotFoundException(String message, int code) {
        super(message, code, HttpStatus.NOT_FOUND);
    }
}
