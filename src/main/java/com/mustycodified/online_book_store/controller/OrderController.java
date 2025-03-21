package com.mustycodified.online_book_store.controller;

import com.mustycodified.online_book_store.dto.request.OrderRequestDto;
import com.mustycodified.online_book_store.dto.response.ApiResponse;
import com.mustycodified.online_book_store.dto.response.OrderResponseDto;
import com.mustycodified.online_book_store.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Validated

public class OrderController {
    private final OrderService orderService;
    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<OrderResponseDto>> createOrder(@Valid @RequestBody OrderRequestDto orderRequestDto) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Request successfully processed", orderService.createOrder(orderRequestDto)));
    }

    @GetMapping("/{userId}/history")
    public ResponseEntity<ApiResponse<ApiResponse.Wrapper<List<OrderResponseDto>>>> fetchOrderHistory(
            @PathVariable(value = "userId") Long userId,
            @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "createdAt", required = false) String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = "desc", required = false) String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        return ResponseEntity.ok(new ApiResponse<>(true, "Request successfully processed", orderService.viewPurchaseHistory(userId, pageable)));
    }

}
