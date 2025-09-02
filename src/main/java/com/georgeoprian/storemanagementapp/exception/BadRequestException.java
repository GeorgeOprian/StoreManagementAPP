package com.georgeoprian.storemanagementapp.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ApiException{

    public BadRequestException(String message, int code) {
        super(message, code, HttpStatus.BAD_REQUEST);
    }

}
