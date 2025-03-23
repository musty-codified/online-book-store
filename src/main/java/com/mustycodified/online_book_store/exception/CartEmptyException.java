package com.mustycodified.online_book_store.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartEmptyException extends RuntimeException {
    private String message;

    public CartEmptyException(String message) {
        super(message);
        this.message = message;
    }
}
