package com.mustycodified.online_book_store.controller;

import com.mustycodified.online_book_store.dto.response.ApiResponse;
import com.mustycodified.online_book_store.dto.CartItemsDto;
import com.mustycodified.online_book_store.dto.response.CartResponseDto;
import com.mustycodified.online_book_store.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
@Validated

public class CartController {
    private final CartService cartService;
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<CartResponseDto>> getCartContent(@PathVariable (value = "userId") @Positive(message = "userId must be positive number") Long userId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Request successfully processed", cartService.viewCartContent(userId)));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<CartItemsDto>> addItemToCart(@PathVariable (value = "userId") Long userId, @Valid @RequestBody CartItemsDto cartItemsDto) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Request successfully processed", cartService.addToCart(userId, cartItemsDto)));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<String>> clearCart(@PathVariable (value = "userId") Long userId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Request successfully processed", cartService.clearCart(userId)));
    }
}
