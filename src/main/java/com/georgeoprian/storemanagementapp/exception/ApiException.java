package com.georgeoprian.storemanagementapp.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApiException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final HttpStatus status;
    private int code = 0;

    public ApiException(String message, HttpStatus httpStatus) {
        super(message);
        status = httpStatus;
    }

}
