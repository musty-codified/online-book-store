package com.mustycodified.online_book_store.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResourceNotFoundException extends RuntimeException {
    private String message;
    private String httpStatus;

    public ResourceNotFoundException(String message, String httpStatus) {
        super(message);
        this.httpStatus = String.valueOf(httpStatus);
        this.message = message;
    }
}
