package com.mustycodified.online_book_store.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mustycodified.online_book_store.config.CustomUserDetailsService;
import com.mustycodified.online_book_store.config.JwtUtils;
import com.mustycodified.online_book_store.dto.request.LoginRequestDto;
import com.mustycodified.online_book_store.dto.request.UserRequestDto;
import com.mustycodified.online_book_store.dto.response.LoginResponseDto;
import com.mustycodified.online_book_store.dto.response.UserResponseDto;
import com.mustycodified.online_book_store.entity.User;
import com.mustycodified.online_book_store.enums.Roles;
import com.mustycodified.online_book_store.exception.CustomValidationException;
import com.mustycodified.online_book_store.exception.ResourceAlreadyExistException;
import com.mustycodified.online_book_store.repository.UserRepository;
import com.mustycodified.online_book_store.service.UserService;
import com.mustycodified.online_book_store.util.AppUtil;
import com.mustycodified.online_book_store.util.CustomMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AppUtil appUtil;
    private final CustomMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtil;
    private final ObjectMapper objectMapper;
    private final CustomUserDetailsService userDetailsService;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @Override
    public UserResponseDto createUser(UserRequestDto requestDto) {
        validateEmail(requestDto.getEmail());
        appUtil.validateEmailDomain(requestDto.getEmail());
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new ResourceAlreadyExistException("User already exists");
        }

       User newUser = objectMapper.convertValue(requestDto, User.class);
        newUser.setRole(Roles.USER.getPermissions().stream()
                .map(Objects::toString).collect(Collectors.joining(",")));
        newUser.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        newUser = userRepository.save(newUser);
        return mapper.mapToUserDto(newUser);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto, HttpServletRequest request) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(loginRequestDto.getEmail());
            if (userOptional.isEmpty()) {
                log.error("User not found with email {}", loginRequestDto.getEmail());
                throw new BadCredentialsException("Invalid credentials");
            }

            User user = userOptional.get();
            if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())){
                log.error("Bad credentials with email {}", loginRequestDto.getEmail());
                throw new BadCredentialsException("Invalid credentials");
            }

            if (user.getRole() == null) {
                throw new IllegalArgumentException("Not assigned to any role; cannot log in");
            }
            final String accessToken = jwtUtil.generateToken(userDetailsService.loadUserByUsername(user.getEmail()));
            return LoginResponseDto.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .accessToken(accessToken)
                    .expiresIn(jwtExpiration)
                    .build();

        } catch (RuntimeException e){
            throw new BadCredentialsException("Incorrect credentials");

        }
    }

    private void validateEmail(String email) {
        if (!AppUtil.isEmailValid(email)) {
            throw new CustomValidationException("Invalid email format {" + email + "}");
        }
    }
}
