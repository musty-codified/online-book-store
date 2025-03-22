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

import java.math.BigDecimal;
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

    UserRequestDto userRequestDto;
    User userEntity;
    UserResponseDto userResponseDto;
    String email = "Johnuser@gmail.com";
    String firstName = "John";
    String lastName = "Doe";
    String password = "Password#123";
    String encodedPassword = "encodedPassword#123";
    String role = "ADMIN";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("createUser_ReturnCreatedUserResponse")
    void testCreateNewUser_ReturnsCreatedUser() {

        userRequestDto = createUserRequestDto();
        userEntity = createUser();
        userResponseDto = createUserResponseDto();

        when(userRepository.existsByEmail(userRequestDto.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(userEntity);
        when(passwordEncoder.encode(userRequestDto.getPassword())).thenReturn(encodedPassword);
        when(objectMapper.convertValue(userRequestDto, User.class)).thenReturn(userEntity);
        when(mapper.mapToUserDto(any(User.class))).thenReturn(userResponseDto);

        UserResponseDto returnValue = userService.createUser(userRequestDto);

        assertNotNull(returnValue);
        assertEquals(userRequestDto.getEmail(), returnValue.getEmail());
        verify(userRepository).existsByEmail(userRequestDto.getEmail());
        verify(userRepository, times(1)).save(userEntity);
        verify(mapper, times(1)).mapToUserDto(userEntity);
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

//    @Test
//    void login_NonexistentEmail_ThrowsResourceNotFoundException() {
//        Long userId = 1L;
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> userService.login(new LoginRequestDto()));
//        assertEquals("User with id " + userId + " does not exist", exception.getMessage());
//    }



    private UserRequestDto createUserRequestDto(){
        userRequestDto = new UserRequestDto();
        userRequestDto.setFirstName(firstName);
        userRequestDto.setLastName(lastName);
        userRequestDto.setEmail(email);
        userRequestDto.setPassword(password);
        return userRequestDto;
    }

    private User createUser(){
        userEntity = new User();
        userEntity.setId(1L);
        userEntity.setFirstName(firstName);
        userEntity.setLastName(lastName);
        userEntity.setEmail(email);
        userEntity.setPassword(encodedPassword);
        userEntity.setRole(role);
        userEntity.setCreatedAt(date());
        userEntity.setUpdatedAt(date());
        return userEntity;
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
