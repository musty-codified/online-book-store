package com.mustycodified.online_book_store.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserRequestDto implements Serializable {
    private static final long serialVersionUID= 1L;

    @NotBlank(message = "First Name is required")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9\\W]).*$",
            message = "Password must contain a number or a symbol and at least one uppercase letter")
    private String password;


}
