package com.mustycodified.online_book_store.service;
import com.mustycodified.online_book_store.dto.response.CartItemsDto;
import com.mustycodified.online_book_store.dto.response.CartResponseDto;


public interface CartService {
    CartResponseDto viewCartContent(Long userId);
    CartItemsDto addToCart(Long userId, CartItemsDto cartItemsDto);

    String clearCart(Long userId);

}
