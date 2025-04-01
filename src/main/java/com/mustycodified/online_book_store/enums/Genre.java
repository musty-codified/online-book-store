package com.mustycodified.online_book_store.enums;

import lombok.Getter;

@Getter
public enum Genre {
    FICTION("Fiction"),
    THRILLER("Thriller"),
    MYSTERY("Mystery"),
    POETRY("Poetry"),
    HORROR("Horror"),
    SATIRE("Satire");

    private final String genre;

    Genre(String genre){
        this.genre = genre;
    }
}
