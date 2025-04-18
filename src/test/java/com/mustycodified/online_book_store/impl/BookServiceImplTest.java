package com.mustycodified.online_book_store.impl;

import com.mustycodified.online_book_store.dto.response.ApiResponse;
import com.mustycodified.online_book_store.dto.response.BookResponseDto;
import com.mustycodified.online_book_store.entity.Book;

import com.mustycodified.online_book_store.repository.BookRepository;
import com.mustycodified.online_book_store.service.impl.BookServiceImpl;
import com.mustycodified.online_book_store.util.CustomMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CustomMapper mapper;
    @InjectMocks
    private BookServiceImpl bookService;

    private Book book1;
    private Book book2;
    private BookResponseDto bookDto1;
    private BookResponseDto bookDto2;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        book1 = new Book();
        book1.setIsbn("978-1-56619-909-4");
        book1.setTitle("Head First Java");

        book2 = new Book();
        book2.setIsbn("438-0-56019-903-4");
        book2.setTitle("Java Spring Boot");

        bookDto1 = new BookResponseDto();
        bookDto1.setId(1L);
        bookDto1.setIsbn(book1.getTitle());
        bookDto1.setTitle(book1.getTitle());

        bookDto2 = new BookResponseDto();
        bookDto2.setId(2L);
        bookDto2.setIsbn(book2.getIsbn());
        bookDto2.setTitle(book2.getTitle());
    }

    @Test
    @DisplayName("getAllBooks_ReturnsBooks")
    final void getAllBooks_ReturnsPaginatedBookList() {

        Pageable pageable = PageRequest.of(0, 5);
        List<Book> bookList = List.of(book1, book2);
        Page<Book> bookPage = new PageImpl<>(bookList, pageable, bookList.size());

        when(bookRepository.fetchAllBooks(anyString(), eq(pageable))).thenReturn(bookPage);

        when(mapper.mapToBookResponseDto(book1)).thenReturn(bookDto1);
        when(mapper.mapToBookResponseDto(book2)).thenReturn(bookDto2);

        ApiResponse.Wrapper<List<BookResponseDto>> returnValue = bookService.getAllBooks("Java", pageable);
        BookResponseDto responseDto = returnValue.getContent().get(0);
        System.out.println(responseDto.getTitle());

        assertNotNull(returnValue);
        assertEquals(2, returnValue.getTotalItems());
        assertEquals("Head First Java", returnValue.getContent().get(0).getTitle());
        verify(bookRepository).fetchAllBooks(eq("Java"), eq(pageable));
        verify(bookRepository, times(0)).save(book1);
        verify(bookRepository, times(0)).save(book2);
        verify(mapper, times(1)).mapToBookResponseDto(book1);
        verify(mapper, times(1)).mapToBookResponseDto(book2);

    }

}
