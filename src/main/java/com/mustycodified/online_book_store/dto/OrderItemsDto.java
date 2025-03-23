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
public class OrderItemsDto {

    @NotNull
    private Long bookId;

    @NotNull
    private Long orderId;
    @Min(1)
    private int quantity;
    @NotNull
    private Double unitPrice;
}
