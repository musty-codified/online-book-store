package com.mustycodified.online_book_store.service.impl;

import com.mustycodified.online_book_store.dto.response.ApiResponse;
import com.mustycodified.online_book_store.dto.response.BookResponseDto;
import com.mustycodified.online_book_store.entity.Book;
import com.mustycodified.online_book_store.exception.ResourceNotFoundException;
import com.mustycodified.online_book_store.repository.BookRepository;
import com.mustycodified.online_book_store.service.BookService;
import com.mustycodified.online_book_store.util.CustomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CustomMapper mapper;

    @Override
    public ApiResponse.Wrapper<List<BookResponseDto>> getAllBooks(String searchText, Pageable pageable) {
        Page<Book> bookPage = bookRepository.fetchAllBooks(searchText != null && !searchText.isEmpty() ? searchText : null, pageable);

        if (bookPage.isEmpty()) {
            throw new ResourceNotFoundException("No Book Record was Found", HttpStatus.NOT_FOUND.toString());
        }
        List<BookResponseDto> responses = bookPage.getContent().stream()
                .map(mapper::mapToBookResponseDto)
                .collect(Collectors.toList());

        return new ApiResponse.Wrapper<>(
                responses,
                bookPage.getTotalPages(),
                bookPage.getTotalElements(),
                bookPage.getNumberOfElements(),
                bookPage.getNumber() + 1,
                bookPage.isLast(),
                bookPage.isFirst(),
                bookPage.isEmpty()
        );
    }

    @Override
    public List<BookResponseDto> getBooks() {
        return bookRepository.findAll().stream().map(mapper::mapToBookResponseDto).toList();
    }


}

