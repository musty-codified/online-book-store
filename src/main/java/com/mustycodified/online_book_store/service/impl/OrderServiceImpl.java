package com.mustycodified.online_book_store.service.impl;

import com.mustycodified.online_book_store.dto.request.OrderRequestDto;
import com.mustycodified.online_book_store.dto.response.ApiResponse;
import com.mustycodified.online_book_store.dto.response.OrderResponseDto;
import com.mustycodified.online_book_store.entity.*;
import com.mustycodified.online_book_store.enums.OrderStatus;
import com.mustycodified.online_book_store.exception.ResourceNotFoundException;
import com.mustycodified.online_book_store.repository.CartRepository;
import com.mustycodified.online_book_store.repository.OrderRepository;
import com.mustycodified.online_book_store.service.CartService;
import com.mustycodified.online_book_store.service.OrderService;
import com.mustycodified.online_book_store.util.CustomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final CustomMapper mapper;
    private final PaymentService paymentService;

    @Override
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        Cart cart = cartRepository.findByUserId(orderRequestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart Not Found", HttpStatus.NOT_FOUND.name()));
        List<CartItem> cartItems = cart.getCartItems();
        double totalPrice = cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        User user = cart.getUser();
        Order order = Order.builder()
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.PENDING.toString())
                .grandTotal(totalPrice)
                .user(user)
                .paymentMethod(orderRequestDto.getPaymentMethod())
                .build();
        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> new OrderItem(cartItem.getBook(), order, cartItem.getQuantity(), cartItem.getPrice())).toList();
        order.setOrderItems(orderItems);
        orderRepository.save(order);
        cartService.clearCart(orderRequestDto.getUserId());

        paymentService.processPayment(order);
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
