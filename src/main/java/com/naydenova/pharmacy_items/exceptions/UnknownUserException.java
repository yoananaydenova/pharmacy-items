package com.naydenova.pharmacy_items.exceptions;

import org.springframework.http.HttpStatus;

public class UnknownUserException extends RuntimeException{

    private final HttpStatus status = HttpStatus.NOT_FOUND;

    public UnknownUserException() {
        super("Unknown user!");
    }

    public HttpStatus getStatus() {
        return status;
    }
}