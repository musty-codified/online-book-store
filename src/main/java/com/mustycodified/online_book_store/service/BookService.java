package com.mustycodified.online_book_store.service;

import com.mustycodified.online_book_store.dto.response.ApiResponse;
import com.mustycodified.online_book_store.dto.response.BookResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    ApiResponse.Wrapper<List<BookResponseDto>> getAllBooks(String searchText, Pageable pageable);
    List<BookResponseDto> getBooks();


}
