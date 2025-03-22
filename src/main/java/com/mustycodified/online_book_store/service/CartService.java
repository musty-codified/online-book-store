package com.mustycodified.online_book_store.service;

import com.mustycodified.online_book_store.dto.CartItemsDto;
import com.mustycodified.online_book_store.dto.response.CartResponseDto;


public interface CartService {
    CartResponseDto viewCartContent(Long userId);

    CartItemsDto addItemToCart(Long userId, CartItemsDto cartItemsDto);

    String clearCart(Long userId);

}
