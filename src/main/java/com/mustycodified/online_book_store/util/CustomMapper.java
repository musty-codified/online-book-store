package com.mustycodified.online_book_store.util;

import com.mustycodified.online_book_store.dto.response.BookResponseDto;
import com.mustycodified.online_book_store.dto.response.UserResponseDto;
import com.mustycodified.online_book_store.entity.Book;
import com.mustycodified.online_book_store.entity.User;
import com.mustycodified.online_book_store.repository.BookRepository;
import com.mustycodified.online_book_store.repository.CartRepository;
import com.mustycodified.online_book_store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CustomMapper {

    public UserResponseDto mapToUserDto(User userEntity) {
        return UserResponseDto.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .build();
    }


    public BookResponseDto mapToBookDto(Book bookEntity) {
        return BookResponseDto.builder()
                .id(bookEntity.getId())
                .title(bookEntity.getTitle())
                .author(bookEntity.getAuthor())
                .genre(bookEntity.getGenre())
                .quantity(bookEntity.getQuantity())
                .publishedYear(bookEntity.getPublishedYear())
                .isbn(bookEntity.getIsbn())
                .build();
    }

}
