package com.mustycodified.online_book_store.impl;

import com.mustycodified.online_book_store.dto.request.OrderRequestDto;
import com.mustycodified.online_book_store.dto.response.ApiResponse;
import com.mustycodified.online_book_store.dto.response.OrderResponseDto;
import com.mustycodified.online_book_store.entity.*;
import com.mustycodified.online_book_store.enums.PaymentMethod;
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

    OrderRequestDto orderRequestDto;
    OrderResponseDto orderResponseDto;
    Long userId = 1L;
    Long orderId = 1L;
    String paymentMethod = "TRANSFER";
    String orderStatus = "COMPLETED";
    Order orderEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("createOrder_ReturnsOrderResponseDto")
    final void testCreateOrder_ReturnsOrderResponseDto() {

        //Order Request DTO
        orderRequestDto = new OrderRequestDto();
        orderRequestDto.setUserId(userId);
        orderRequestDto.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));

        Book bookEntity1 = new Book();
        bookEntity1.setTitle("Head First Java");

        Book bookEntity2 = new Book();
        bookEntity2.setTitle("Java Spring Boot");

        CartItem cartItemEntity1 = new CartItem();
        cartItemEntity1.setId(1L);
        cartItemEntity1.setBook(bookEntity1);
        cartItemEntity1.setPrice(40.0);
        cartItemEntity1.setQuantity(4);

        CartItem cartItemEntity2 = new CartItem();
        cartItemEntity2.setId(2L);
        cartItemEntity2.setBook(bookEntity2);
        cartItemEntity2.setPrice(15.0);
        cartItemEntity2.setQuantity(3);

        User userEntity = new User();
        userEntity.setId(userId);

        Cart cartEntity = new Cart();
        cartEntity.setId(1L);
        cartEntity.setUser(userEntity);
        cartEntity.setCartItems(List.of(cartItemEntity1, cartItemEntity2));

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setId(1L);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setId(2L);

        orderEntity = new Order();
        orderEntity.setId(orderId);
        orderEntity.setUser(userEntity);
        orderEntity.setOrderItems(List.of(orderItem1, orderItem2));
        orderEntity.setOrderStatus(orderStatus);
        orderEntity.setGrandTotal(205.0);

        //Response dto
        orderResponseDto = new OrderResponseDto();
        orderResponseDto.setUserId(userId);
        orderResponseDto.setOrderStatus(orderStatus);

        // Mocks
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cartEntity));
        when(orderRepository.save(any(Order.class))).thenReturn(orderEntity);
        when(mapper.mapToOrderResponseDto(orderEntity)).thenReturn(orderResponseDto);

        //when
        OrderResponseDto returnValue = orderService.createOrder(orderRequestDto);

        //Then
        assertNotNull(returnValue);
        assertEquals(userId, returnValue.getUserId());
        assertEquals(orderResponseDto.getOrderStatus(), returnValue.getOrderStatus());
        verify(cartRepository).findByUserId(userId);
        verify(orderRepository).save(any(Order.class));
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(cartService).clearCart(userId);
        verify(paymentService).processPayment(any(Order.class));
        verify(mapper).mapToOrderResponseDto(any(Order.class));

    }

    @Test
    @DisplayName("viewPurchaseHistory_ReturnsOrderResponseDtoList")
    final void testViewPurchaseHistory_ReturnsOrderResponseDtoList() {

       Order orderEntity1 = new Order();
       orderEntity1.setId(1L);
       orderEntity1.setOrderDate(LocalDateTime.now());

        Order orderEntity2 = new Order();
        orderEntity2.setId(2L);
        orderEntity2.setOrderDate(LocalDateTime.now().minusDays(3));

        OrderResponseDto orderResponseDto1 = new OrderResponseDto();
        orderResponseDto1.setOrderStatus(orderStatus);

       OrderResponseDto orderResponseDto2 = new OrderResponseDto();
        orderResponseDto2.setOrderStatus(orderStatus);

        Pageable pageable = PageRequest.of(0, 5);
        List<Order> orderList = List.of(orderEntity1, orderEntity2);
        Page<Order> orderPage = new PageImpl<>(orderList, pageable, orderList.size());

        when(orderRepository.fetchAllOrders(anyLong(), eq(pageable))).thenReturn(orderPage);

        when(mapper.mapToOrderResponseDto(orderEntity1)).thenReturn(orderResponseDto1);
        when(mapper.mapToOrderResponseDto(orderEntity2)).thenReturn(orderResponseDto2);

        ApiResponse.Wrapper<List<OrderResponseDto>> returnValue = orderService.viewPurchaseHistory(userId, pageable);
        OrderResponseDto responseDto = returnValue.getContent().get(0);
        System.out.println(responseDto.getOrderStatus());

        assertNotNull(returnValue);
        assertEquals(2, returnValue.getTotalItems());
        assertEquals("COMPLETED", returnValue.getContent().get(0).getOrderStatus());
        verify(orderRepository).fetchAllOrders(anyLong(), eq(pageable));
        verify(orderRepository, times(0)).save(orderEntity1);
        verify(orderRepository, times(0)).save(orderEntity2);
        verify(mapper, times(1)).mapToOrderResponseDto(orderEntity1);
        verify(mapper, times(1)).mapToOrderResponseDto(orderEntity2);

    }


}
