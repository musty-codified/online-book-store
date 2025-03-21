package com.mustycodified.online_book_store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookResponseDto implements Serializable {
    private Long id;
    private String title;
    private String author;
    private String genre;
    private String isbn;
    private int publishedYear;

}
