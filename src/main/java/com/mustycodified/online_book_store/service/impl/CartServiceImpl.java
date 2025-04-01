package com.mustycodified.online_book_store.service.impl;

import org.springframework.transaction.annotation.Transactional;
import com.mustycodified.online_book_store.dto.CartItemsDto;
import com.mustycodified.online_book_store.dto.response.CartResponseDto;
import com.mustycodified.online_book_store.entity.Cart;
import com.mustycodified.online_book_store.entity.CartItem;
import com.mustycodified.online_book_store.entity.User;
import com.mustycodified.online_book_store.exception.ResourceNotFoundException;
import com.mustycodified.online_book_store.repository.CartItemRepository;
import com.mustycodified.online_book_store.repository.CartRepository;
import com.mustycodified.online_book_store.repository.UserRepository;
import com.mustycodified.online_book_store.service.CartService;
import com.mustycodified.online_book_store.util.CustomMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final CustomMapper mapper;

    @Override
    public CartResponseDto viewCartContent(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found", HttpStatus.NOT_FOUND.name()));
            return cartRepository.save(Cart.builder().user(user).build());
        });
        return mapper.mapToCartDto(cart);
    }

    @Override
    public CartItemsDto addItemToCart(Long userId, CartItemsDto cartItemsDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found", HttpStatus.NOT_FOUND.name()));
        Cart cart = user.getCart();
        if (cart == null) {
            cart = cartRepository.save(Cart.builder().user(user).build());
        }
        List<CartItem> existingItems = cart.getCartItems();
        CartItem existingItem = existingItems.stream()
                .filter(item -> item.getBook().getId().equals(cartItemsDto.getBookId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + cartItemsDto.getQuantity());
            cartItemRepository.save(existingItem);
            return mapper.mapToCartItemsDto(existingItem);
        }
        CartItem cartItem = mapper.mapToCartItems(cartItemsDto);
        cartItem.setCart(cart);
        CartItem savedItem = cartItemRepository.save(cartItem);
        cart.getCartItems().add(savedItem);
        cartRepository.save(cart);
        return mapper.mapToCartItemsDto(savedItem);

    }

    @Override
    @Transactional
    public String clearCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found", HttpStatus.NOT_FOUND.name()));
        Cart cart = user.getCart();
        if (cart == null || cart.getCartItems().isEmpty()) {
            return "Cart is already empty";
        }
        cart.getCartItems().forEach(cartItem -> cartItemRepository.deleteById(cartItem.getId()));
        cart.getCartItems().clear();
        cart.setCartItems(cart.getCartItems());
        cartRepository.save(cart);
        log.info("Cart ID: {} has been cleared for user ID :{}", cart.getId(), user.getId());

        return "Cart cleared successfully";
    }
}
