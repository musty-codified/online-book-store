package com.mustycodified.online_book_store.service.impl;

import com.mustycodified.online_book_store.dto.request.UserRequestDto;
import com.mustycodified.online_book_store.dto.response.UserResponseDto;
import com.mustycodified.online_book_store.entity.User;
import com.mustycodified.online_book_store.enums.Roles;
import com.mustycodified.online_book_store.exception.CustomValidationException;
import com.mustycodified.online_book_store.exception.ResourceAlreadyExistException;
import com.mustycodified.online_book_store.repository.UserRepository;
import com.mustycodified.online_book_store.service.UserService;
import com.mustycodified.online_book_store.util.AppUtil;
import com.mustycodified.online_book_store.util.CustomMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AppUtil appUtil;
    private final CustomMapper mapper;


    @Override
    public UserResponseDto createUser(UserRequestDto requestDto) {
        validateEmail(requestDto.getEmail());
        appUtil.validateEmailDomain(requestDto.getEmail());
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new ResourceAlreadyExistException("User already exists");
        }

       User newUser = appUtil.getMapper().convertValue(requestDto, User.class);
        newUser.setRole(Roles.USER.getPermissions().stream()
                .map(Objects::toString).collect(Collectors.joining(",")));
        newUser.setPassword(requestDto.getPassword());

        newUser = userRepository.save(newUser);
        return mapper.mapToUserDto(newUser);
    }


    private void validateEmail(String email) {
        if (!AppUtil.isEmailValid(email)) {
            throw new CustomValidationException("Invalid email format {" + email + "}");
        }
    }
}
