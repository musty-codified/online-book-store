package com.mustycodified.online_book_store.service.impl;

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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
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
        CartItem cartItem = mapper.mapToCartItems(cartItemsDto);
        assert cart != null;
        List<CartItem> items = cart.getCartItems();
        cart.getCartItems().add(cartItem);
        cart.setCartItems(items);
        return mapper.mapToCartItemsDto(cartItemRepository.save(cartItem));

    }

    @Override
    public String clearCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found", HttpStatus.NOT_FOUND.name()));
        Cart cart = user.getCart();
        List<CartItem> cartItems = cart.getCartItems();

        for (CartItem item : cartItems) {
            cartItemRepository.deleteById(item.getId());
        }

        cartItems.clear();
        cart.setCartItems(cartItems);
        cartRepository.save(cart);
        return "Cart cleared successfully";
    }
}
