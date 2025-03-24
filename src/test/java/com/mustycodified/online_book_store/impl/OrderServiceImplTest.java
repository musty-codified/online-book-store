package com.mustycodified.online_book_store.impl;

import com.mustycodified.online_book_store.dto.request.OrderRequestDto;
import com.mustycodified.online_book_store.dto.response.ApiResponse;
import com.mustycodified.online_book_store.dto.response.OrderResponseDto;
import com.mustycodified.online_book_store.entity.*;
import com.mustycodified.online_book_store.enums.PaymentMethod;
import com.mustycodified.online_book_store.exception.ResourceNotFoundException;
import com.mustycodified.online_book_store.repository.CartRepository;
import com.mustycodified.online_book_store.repository.OrderRepository;
import com.mustycodified.online_book_store.service.CartService;
import com.mustycodified.online_book_store.service.impl.OrderServiceImpl;
import com.mustycodified.online_book_store.service.impl.PaymentService;
import com.mustycodified.online_book_store.util.CustomMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class OrderServiceImplTest {
    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartService cartService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomMapper mapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderRequestDto orderRequestDto;
    private OrderResponseDto orderResponseDto;;
    private Order order;
    private Cart cart;
    private User user;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);

        //Order Request DTO
        orderRequestDto = new OrderRequestDto();
        orderRequestDto.setUserId(user.getId());
        orderRequestDto.setPaymentMethod(PaymentMethod.valueOf("TRANSFER"));

        Book book1 = new Book();
        book1.setTitle("Head First Java");

        Book book2 = new Book();
        book2.setTitle("Effective Spring Boot");

        CartItem cartItem1 = new CartItem();
        cartItem1.setId(1L);
        cartItem1.setBook(book1);
        cartItem1.setPrice(40.0);
        cartItem1.setQuantity(4);

        CartItem cartItem2 = new CartItem();
        cartItem2.setId(2L);
        cartItem2.setBook(book2);
        cartItem2.setPrice(15.0);
        cartItem2.setQuantity(3);

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setCartItems(List.of(cartItem1, cartItem2));

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setId(1L);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setId(2L);

        order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setOrderItems(List.of(orderItem1, orderItem2));
        order.setOrderStatus("COMPLETED");
        order.setGrandTotal(205.0);

        //Response dto
        orderResponseDto = new OrderResponseDto();
        orderResponseDto.setUserId(user.getId());
        orderResponseDto.setOrderStatus("COMPLETED");

    }

    @Test
    @DisplayName("createOrder_ReturnsOrderDto")
    final void testCreateOrder_ReturnsOrderResponseDto() {
        // Mocks
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(mapper.mapToOrderResponseDto(order)).thenReturn(orderResponseDto);

        //when
        OrderResponseDto returnValue = orderService.createOrder(orderRequestDto);

        //Then
        assertNotNull(returnValue);
        assertEquals(user.getId(), returnValue.getUserId());
        assertEquals(orderResponseDto.getOrderStatus(), returnValue.getOrderStatus());
        verify(cartRepository).findByUserId(user.getId());
        verify(orderRepository).save(any(Order.class));
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(cartService).clearCart(user.getId());
        verify(paymentService).processPayment(any(Order.class));
        verify(mapper).mapToOrderResponseDto(any(Order.class));

    }

    @Test
    void testCreateOrder_CartNotFound_ThrowsException() {
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                orderService.createOrder(orderRequestDto));

        assertEquals("Cart Not Found", exception.getMessage());
        verifyNoInteractions(orderRepository);
        verifyNoInteractions(cartService);
        verifyNoInteractions(paymentService);
    }

    @Test
    @DisplayName("viewPurchaseHistory_ReturnsOrderList")
    final void testViewPurchaseHistory_ReturnsOrderResponseDtoList() {
        Order order1 = new Order();
        order1.setId(1L);
        order1.setOrderDate(LocalDateTime.now());

        Order order2 = new Order();
        order2.setId(2L);
        order2.setOrderDate(LocalDateTime.now().minusDays(3));

        OrderResponseDto orderResponseDto1 = new OrderResponseDto();
        orderResponseDto1.setOrderStatus("COMPLETED");

        OrderResponseDto orderResponseDto2 = new OrderResponseDto();
        orderResponseDto2.setOrderStatus("COMPLETED");

        Pageable pageable = PageRequest.of(0, 5);
        List<Order> orderList = List.of(order1, order2);
        Page<Order> orderPage = new PageImpl<>(orderList, pageable, orderList.size());

        when(orderRepository.fetchAllOrders(anyLong(), eq(pageable))).thenReturn(orderPage);
        when(mapper.mapToOrderResponseDto(order1)).thenReturn(orderResponseDto1);
        when(mapper.mapToOrderResponseDto(order2)).thenReturn(orderResponseDto2);

        ApiResponse.Wrapper<List<OrderResponseDto>> returnValue = orderService.viewPurchaseHistory(user.getId(), pageable);
        OrderResponseDto responseDto = returnValue.getContent().get(0);
        System.out.println(responseDto.getOrderStatus());

        assertNotNull(returnValue);
        assertEquals(2, returnValue.getTotalItems());
        assertEquals("COMPLETED", returnValue.getContent().get(0).getOrderStatus());
        verify(orderRepository).fetchAllOrders(anyLong(), eq(pageable));
        verify(orderRepository, times(0)).save(order1);
        verify(orderRepository, times(0)).save(order2);
        verify(mapper, times(1)).mapToOrderResponseDto(order1);
        verify(mapper, times(1)).mapToOrderResponseDto(order2);

    }


}
