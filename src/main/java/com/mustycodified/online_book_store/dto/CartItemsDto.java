package com.mustycodified.online_book_store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemsDto {
    private Long bookId;
    private int quantity;
    private double price;
}
