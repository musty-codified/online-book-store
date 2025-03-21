package com.mustycodified.online_book_store.service;
import com.mustycodified.online_book_store.dto.response.ApiResponse;
import com.mustycodified.online_book_store.dto.response.OrderResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(Long userId);
    ApiResponse.Wrapper<List<OrderResponseDto>> viewPurchaseHistory(Long userId, Pageable pageable);
}
