package com.mustycodified.online_book_store.service;

import com.mustycodified.online_book_store.dto.request.LoginRequestDto;
import com.mustycodified.online_book_store.dto.request.UserRequestDto;
import com.mustycodified.online_book_store.dto.response.LoginResponseDto;
import com.mustycodified.online_book_store.dto.response.UserResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    UserResponseDto createUser(UserRequestDto requestDto);
    LoginResponseDto login (LoginRequestDto loginRequestDto, HttpServletRequest request);

}

