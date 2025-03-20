package com.mustycodified.online_book_store.service;

import com.mustycodified.online_book_store.dto.request.UserRequestDto;
import com.mustycodified.online_book_store.dto.response.UserResponseDto;

public interface UserService {
    UserResponseDto createUser(UserRequestDto requestDto);

}

