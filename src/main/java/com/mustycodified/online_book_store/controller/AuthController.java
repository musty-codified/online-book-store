package com.mustycodified.online_book_store.controller;

import com.mustycodified.online_book_store.dto.request.LoginRequestDto;
import com.mustycodified.online_book_store.dto.response.ApiResponse;
import com.mustycodified.online_book_store.dto.response.LoginResponseDto;
import com.mustycodified.online_book_store.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@Validated @RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request) {
        return ResponseEntity.ok().body(new ApiResponse<>(true, "Login Successful", userService.login(loginRequestDto, request)));
    }
}
