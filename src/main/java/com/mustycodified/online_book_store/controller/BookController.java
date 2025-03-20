package com.mustycodified.online_book_store.controller;


import com.mustycodified.online_book_store.dto.response.ApiResponse;
import com.mustycodified.online_book_store.dto.response.BookResponseDto;
import com.mustycodified.online_book_store.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<ApiResponse<ApiResponse.Wrapper<List<BookResponseDto>>>> fetchAllBooks(
            @RequestParam(required = false) String searchText,
            @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "createdAt", required = false) String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = "desc", required = false) String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        return ResponseEntity.ok(new ApiResponse<>(true, "Request successfully processed", bookService.getAllBooks(searchText, pageable)));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<BookResponseDto>>> getBooks() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Request successfully processed", bookService.getBooks()));
    }


}
