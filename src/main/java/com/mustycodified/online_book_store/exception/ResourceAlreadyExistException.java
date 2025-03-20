package com.mustycodified.online_book_store.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResourceAlreadyExistException extends RuntimeException{
    public ResourceAlreadyExistException(String message) {
        super(message);
        this.status = HttpStatus.CONFLICT;
    }

    private final HttpStatus status;
}
