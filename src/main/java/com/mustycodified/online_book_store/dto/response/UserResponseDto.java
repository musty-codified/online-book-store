package com.mustycodified.online_book_store.dto.response;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponseDto implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

}
