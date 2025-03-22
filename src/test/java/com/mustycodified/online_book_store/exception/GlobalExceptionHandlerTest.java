package com.mustycodified.online_book_store.exception;

import com.mustycodified.online_book_store.dto.response.ApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {
    @Mock
    private UnAuthorizedException unAuthorizedException;
    @Mock
    private ResourceAlreadyExistException resourceAlreadyExistException;
    @Mock
    private IllegalArgumentException illegalArgumentException;
    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void handleAccessDeniedException() {
        when(unAuthorizedException.getMessage()).thenReturn("Unauthorized");
        ResponseEntity<ApiResponse<String>> response = globalExceptionHandler.handleUnauthorizedException(unAuthorizedException);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void handleUserAlreadyExistException() {
        when(resourceAlreadyExistException.getMessage()).thenReturn("User already exists");
        when(resourceAlreadyExistException.getStatus()).thenReturn(HttpStatus.BAD_REQUEST);
        ResponseEntity<ApiResponse<String>> response = globalExceptionHandler.handleResourceAlreadyExistException(resourceAlreadyExistException);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void handleIllegalArgumentException() {
        when(illegalArgumentException.getMessage()).thenReturn("Illegal argument");
        ResponseEntity<ApiResponse<String>> response = globalExceptionHandler.handleIllegalArgumentException(illegalArgumentException);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}