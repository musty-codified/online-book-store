package com.mustycodified.online_book_store.dto.response;

import com.mustycodified.online_book_store.dto.OrderItemsDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDto {
    private Long userId;
    private double grandTotal;
    private String orderStatus;
    private List<OrderItemsDto> orderItemsDto;
}
