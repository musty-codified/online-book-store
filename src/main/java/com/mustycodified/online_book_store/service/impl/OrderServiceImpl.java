package com.mustycodified.online_book_store.service.impl;

import com.mustycodified.online_book_store.dto.OrderItemsDto;
import com.mustycodified.online_book_store.dto.response.ApiResponse;
import com.mustycodified.online_book_store.dto.response.BookResponseDto;
import com.mustycodified.online_book_store.dto.response.OrderResponseDto;
import com.mustycodified.online_book_store.entity.*;
import com.mustycodified.online_book_store.enums.OrderStatus;
import com.mustycodified.online_book_store.enums.PaymentMethod;
import com.mustycodified.online_book_store.exception.BookNotFoundException;
import com.mustycodified.online_book_store.exception.CustomValidationException;
import com.mustycodified.online_book_store.exception.ResourceNotFoundException;
import com.mustycodified.online_book_store.repository.CartRepository;
import com.mustycodified.online_book_store.repository.OrderRepository;
import com.mustycodified.online_book_store.repository.UserRepository;
import com.mustycodified.online_book_store.service.CartService;
import com.mustycodified.online_book_store.service.OrderService;
import com.mustycodified.online_book_store.util.CustomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final CustomMapper mapper;

    @Override
    public OrderResponseDto createOrder(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart Not found", HttpStatus.NOT_FOUND.name()));
        List<CartItem> cartItems = cart.getCartItems();
        double totalPrice = cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();

        List<OrderItem> orderItems = new ArrayList<>();
        User user = cart.getUser();
        BigDecimal balance = cart.getUser().getWalletBalance();
        BigDecimal grandTotal = new BigDecimal(totalPrice);

        if(balance.compareTo(grandTotal) < 0)
            throw new CustomValidationException("Insufficient balance");

      BigDecimal newBalance = user.getWalletBalance().subtract(grandTotal);
      user.setWalletBalance(newBalance);

        Order order = Order.builder()
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.PENDING.toString())
                .grandTotal(totalPrice)
                .user(user)
                .paymentMethod(PaymentMethod.TRANSFER)
                .build();

        for (CartItem cartItem : cartItems) {
            orderItems.add(new OrderItem(cartItem.getBook(), order, cartItem.getQuantity(), cartItem.getPrice()));
        }
        order.setOrderItems(orderItems);
        orderRepository.save(order);
        userRepository.save(user);
        cartService.clearCart(userId);

        return mapper.mapToOrderResponseDto(order);
    }

    @Override
    public ApiResponse.Wrapper<List<OrderResponseDto>> viewPurchaseHistory(Long userId, Pageable pageable) {
        Page<Order> orderPage = orderRepository.fetchAllOrders(userId, pageable);

        if (orderPage.isEmpty()) {
            throw new ResourceNotFoundException("No Record was found", HttpStatus.NOT_FOUND.toString());
        }
        List<OrderResponseDto> responses = orderPage.getContent().stream()
                .map(mapper::mapToOrderResponseDto)
                .collect(Collectors.toList());


        return new ApiResponse.Wrapper<>(
                responses,
                orderPage.getTotalPages(),
                orderPage.getTotalElements(),
                orderPage.getNumberOfElements(),
                orderPage.getNumber() + 1,
                orderPage.isLast(),
                orderPage.isFirst(),
                orderPage.isEmpty()
        );

    }
}
