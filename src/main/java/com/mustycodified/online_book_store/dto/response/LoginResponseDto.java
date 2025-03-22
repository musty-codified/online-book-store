package com.mustycodified.online_book_store.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginResponseDto implements Serializable {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;

    @JsonIgnore
    private String role;
    private String accessToken;
    private Long expiresIn;
}
