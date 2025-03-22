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

import java.time.LocalDateTime;
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

    Book bookEntity1;
    Book bookEntity2;
    BookResponseDto bookResponseDto1;
    BookResponseDto bookResponseDto2;
    String searchText = "Java";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("getAllBooks_ReturnsPaginatedBooks")
    final void getAllBooks_ReturnsPaginatedBookList(){

        bookEntity1 = new Book();
        bookEntity1.setIsbn("1023-1234567");
        bookEntity1.setTitle("Head First Java");

        bookEntity2 = new Book();
        bookEntity2.setIsbn("1234-1234567");
        bookEntity2.setTitle("Java Spring Boot");

        bookResponseDto1 = new BookResponseDto();
        bookResponseDto1.setId(1L);
        bookResponseDto1.setIsbn(bookEntity1.getTitle());
        bookResponseDto1.setTitle(bookEntity1.getTitle());

        bookResponseDto2 = new BookResponseDto();
        bookResponseDto2.setId(2L);
        bookResponseDto2.setIsbn(bookEntity2.getIsbn());
        bookResponseDto2.setTitle(bookEntity2.getTitle());

        Pageable pageable = PageRequest.of(0, 5);
        List<Book> bookList = List.of(bookEntity1, bookEntity2);
        Page<Book> bookPage = new PageImpl<>(bookList, pageable, bookList.size());

        when(bookRepository.fetchAllBooks(anyString(), eq(pageable))).thenReturn(bookPage);

        when(mapper.mapToBookResponseDto(bookEntity1)).thenReturn(bookResponseDto1);
        when(mapper.mapToBookResponseDto(bookEntity2)).thenReturn(bookResponseDto2);

        ApiResponse.Wrapper<List<BookResponseDto>> returnValue = bookService.getAllBooks(searchText, pageable);
       BookResponseDto responseDto = returnValue.getContent().get(0);
        System.out.println(responseDto.getTitle());

        assertNotNull(returnValue);
        assertEquals(2, returnValue.getTotalItems());
        assertEquals("Head First Java", returnValue.getContent().get(0).getTitle());
        verify(bookRepository).fetchAllBooks(eq(searchText), eq(pageable));
        verify(bookRepository, times(0)).save(bookEntity1);
        verify(bookRepository, times(0)).save(bookEntity2);
        verify(mapper, times(1)).mapToBookResponseDto(bookEntity1);
        verify(mapper, times(1)).mapToBookResponseDto(bookEntity2);

    }

    private LocalDateTime date() {
        return LocalDateTime.now().minusDays(3);
    }


}
