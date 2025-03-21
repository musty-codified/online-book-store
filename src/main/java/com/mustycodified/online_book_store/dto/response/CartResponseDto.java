package com.mustycodified.online_book_store.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponseDto {
    private Long userId;
    private List<CartItemsDto> cartItemsDto;
}
