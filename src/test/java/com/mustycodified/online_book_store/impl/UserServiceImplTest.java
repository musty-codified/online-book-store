package com.mustycodified.online_book_store.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mustycodified.online_book_store.dto.request.UserRequestDto;
import com.mustycodified.online_book_store.dto.response.UserResponseDto;

import com.mustycodified.online_book_store.entity.User;
import com.mustycodified.online_book_store.exception.ResourceAlreadyExistException;
import com.mustycodified.online_book_store.repository.UserRepository;
import com.mustycodified.online_book_store.service.impl.UserServiceImpl;
import com.mustycodified.online_book_store.util.AppUtil;
import com.mustycodified.online_book_store.util.CustomMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private CustomMapper mapper;
    @Mock
    private AppUtil appUtil;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private UserServiceImpl userService;

    private UserRequestDto userRequestDto;
    private User user;
    private UserResponseDto userResponseDto;

    String email = "Johnuser@gmail.com";
    String firstName = "John";
    String lastName = "Doe";
    String password = "Password#123";
    String encodedPassword = "encodedPassword#123";
    String role = "ADMIN";
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userRequestDto = createUserRequestDto();
        user = createUser();
        userResponseDto = createUserResponseDto();
    }

    @Test
    @DisplayName("createUser_ReturnCreatedUserResponse")
    void testCreateNewUser_ReturnsCreatedUser() {

        when(userRepository.existsByEmail(userRequestDto.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(userRequestDto.getPassword())).thenReturn(encodedPassword);
        when(objectMapper.convertValue(userRequestDto, User.class)).thenReturn(user);
        when(mapper.mapToUserDto(any(User.class))).thenReturn(userResponseDto);

        UserResponseDto returnValue = userService.createUser(userRequestDto);

        assertNotNull(returnValue);
        assertEquals(userRequestDto.getEmail(), returnValue.getEmail());
        verify(userRepository).existsByEmail(userRequestDto.getEmail());
        verify(userRepository, times(1)).save(user);
        verify(mapper, times(1)).mapToUserDto(user);
        verify(appUtil, times(1)).validateEmailDomain(email);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("createUser_ResourceAlreadyExistException")
    void testCreateNewUser_DuplicateEmail_ThrowsAlreadyExistsException() {
        userRequestDto = createUserRequestDto();
        doNothing().when(appUtil).validateEmailDomain(userRequestDto.getEmail());
        when(userRepository.existsByEmail(userRequestDto.getEmail())).thenReturn(true);
        ResourceAlreadyExistException exception = assertThrows(ResourceAlreadyExistException.class, () -> userService.createUser(userRequestDto));
        assertEquals("User already exists", exception.getMessage());
    }

    private UserRequestDto createUserRequestDto(){
        userRequestDto = new UserRequestDto();
        userRequestDto.setFirstName(firstName);
        userRequestDto.setLastName(lastName);
        userRequestDto.setEmail(email);
        userRequestDto.setPassword(password);
        return userRequestDto;
    }

    private User createUser(){
        user = new User();
        user.setId(1L);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setRole(role);
        user.setCreatedAt(date());
        user.setUpdatedAt(date());
        return user;
    }

    private UserResponseDto createUserResponseDto(){
        return  UserResponseDto.builder()
                .id(1L)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }

    private LocalDateTime date() {
        return LocalDateTime.now().minusDays(3);
    }
}
