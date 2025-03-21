package com.mustycodified.online_book_store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemsDto {
    private Long bookId;
    private Long orderId;
    private int quantity;
    private Double unitPrice;
}
