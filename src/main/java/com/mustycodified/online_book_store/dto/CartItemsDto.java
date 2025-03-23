package com.mustycodified.online_book_store.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemsDto {

    @NotNull
    private Long bookId;

    @Min(1)
    private int quantity;
    @NotNull
    private double price;
}
