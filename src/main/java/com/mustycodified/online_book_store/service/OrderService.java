package com.mustycodified.online_book_store.service;
import com.mustycodified.online_book_store.dto.request.OrderRequestDto;
import com.mustycodified.online_book_store.dto.response.ApiResponse;
import com.mustycodified.online_book_store.dto.response.OrderResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto orderRequestDto);
    ApiResponse.Wrapper<List<OrderResponseDto>> viewPurchaseHistory(Long userId, Pageable pageable);
}
