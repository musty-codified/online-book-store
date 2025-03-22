package com.mustycodified.online_book_store.util;

import com.mustycodified.online_book_store.dto.OrderItemsDto;
import com.mustycodified.online_book_store.dto.response.BookResponseDto;
import com.mustycodified.online_book_store.dto.CartItemsDto;
import com.mustycodified.online_book_store.dto.response.CartResponseDto;
import com.mustycodified.online_book_store.dto.response.OrderResponseDto;
import com.mustycodified.online_book_store.dto.response.UserResponseDto;
import com.mustycodified.online_book_store.entity.*;

import com.mustycodified.online_book_store.exception.ResourceNotFoundException;
import com.mustycodified.online_book_store.repository.BookRepository;
import com.mustycodified.online_book_store.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomMapper {
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;

    public UserResponseDto mapToUserDto(User userEntity) {
        return UserResponseDto.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .build();
    }

    public BookResponseDto mapToBookResponseDto(Book bookEntity) {
        return BookResponseDto.builder()
                .id(bookEntity.getId())
                .title(bookEntity.getTitle())
                .author(bookEntity.getAuthor())
                .genre(bookEntity.getGenre())
                .publishedYear(bookEntity.getPublishedYear())
                .isbn(bookEntity.getIsbn())
                .build();
    }

    public CartResponseDto mapToCartDto(Cart cartEntity) {
      Cart cart = cartRepository.findById(cartEntity.getId())
              .orElseThrow(()-> new ResourceNotFoundException("Cart Not found", HttpStatus.NOT_FOUND.name()));
              List<CartItem> cartItems = cart.getCartItems();
              List<CartItemsDto> cartItemsDto = cartItems.stream().map(this::mapToCartItemsDto).toList();
              return CartResponseDto.builder()
                      .userId(cart.getUser().getId())
                      .cartItemsDto(cartItemsDto)
                      .build();

    }

    public CartItemsDto mapToCartItemsDto(CartItem cartItem) {
        return CartItemsDto.builder()
                .bookId(cartItem.getBook().getId())
                .quantity(cartItem.getQuantity())
                .price(cartItem.getPrice())
                .build();
    }

    public CartItem mapToCartItems(CartItemsDto cartItemsDto) {
      Book book = bookRepository.findById(cartItemsDto.getBookId())
              .orElseThrow(()-> new ResourceNotFoundException("Book not found", HttpStatus.NOT_FOUND.name()));
        return CartItem.builder()
                .book(book)
                .price(cartItemsDto.getPrice())
                .quantity(cartItemsDto.getQuantity())
                .build();
    }


    public OrderResponseDto mapToOrderResponseDto(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        List<OrderItemsDto> orderItemsDto = orderItems.stream().map(this::mapToOrderItemsDto).toList();
        return OrderResponseDto.builder()
                .grandTotal(order.getGrandTotal())
                .orderStatus(order.getOrderStatus())
                .userId(order.getUser().getId())
                .orderItemsDto(orderItemsDto)
                .build();
    }

    private OrderItemsDto mapToOrderItemsDto(OrderItem orderItem){
        return OrderItemsDto.builder()
                .orderId(orderItem.getOrder().getId())
                .bookId(orderItem.getBook().getId())
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getUnitPrice())
                .build();
    }


}
