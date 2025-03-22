package com.mustycodified.online_book_store.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequestDto implements Serializable {
    private static final long serialVersionUID= 1L;
    private String email;
    private String password;
}
