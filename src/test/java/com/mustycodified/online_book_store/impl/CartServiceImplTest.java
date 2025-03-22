package com.mustycodified.online_book_store.impl;

import com.mustycodified.online_book_store.dto.CartItemsDto;
import com.mustycodified.online_book_store.dto.response.CartResponseDto;
import com.mustycodified.online_book_store.entity.Cart;
import com.mustycodified.online_book_store.entity.CartItem;
import com.mustycodified.online_book_store.entity.User;
import com.mustycodified.online_book_store.repository.CartItemRepository;
import com.mustycodified.online_book_store.repository.CartRepository;
import com.mustycodified.online_book_store.repository.UserRepository;
import com.mustycodified.online_book_store.service.impl.CartServiceImpl;
import com.mustycodified.online_book_store.util.CustomMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CartServiceImplTest {
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CustomMapper mapper;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CartServiceImpl cartService;

    Cart cartEntity;
    List<CartItem> cartItemList = new ArrayList<>();

    CartItemsDto cartItemsDto;
    Long userId = 1L;

    CartResponseDto cartResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("addItemToCart_ReturnsCartItemDto")
    final void testAddItemToCart_ReturnsCartItemResponseDto() {

        //Request DTO
        cartItemsDto = new CartItemsDto();
        cartItemsDto.setBookId(1L);
        cartItemsDto.setQuantity(5);

        CartItem cartItemEntity = new CartItem();
        cartItemEntity.setId(1L);

        //Response dto
        CartItemsDto expectedResponse = new CartItemsDto();
        cartItemsDto.setBookId(1L);
        cartItemsDto.setQuantity(5);

        cartEntity = new Cart();
        cartEntity.setCartItems(cartItemList);

        User userEntity = new User();
        userEntity.setId(userId);
        userEntity.setCart(cartEntity);

        // Mocks
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(mapper.mapToCartItems(cartItemsDto)).thenReturn(cartItemEntity);
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItemEntity);
        when(mapper.mapToCartItemsDto(any(CartItem.class))).thenReturn(expectedResponse);

        //when
        CartItemsDto returnValue = cartService.addItemToCart(userId, cartItemsDto);

        //Then
        assertNotNull(returnValue);
        assertEquals(expectedResponse.getBookId(), returnValue.getBookId());
        verify(userRepository).findById(userId);
        verify(cartItemRepository).save(cartItemEntity);
        verify(mapper).mapToCartItemsDto(any(CartItem.class));
        verify(cartItemRepository, times(1)).save(any(CartItem.class));

    }

    @Test
    @DisplayName("viewCartContent_ReturnsCartResponseDto")
    final void testViewCartContent_ReturnsCartResponseDto() {

        //Response dto
        cartResponseDto = new CartResponseDto();
        cartResponseDto.setUserId(userId);
        cartResponseDto.setCartItemsDto(new ArrayList<>());

        cartEntity = new Cart();
        cartEntity.setCartItems(cartItemList);

        User userEntity = new User();
        userEntity.setId(userId);
        userEntity.setCart(cartEntity);

        // Mocks
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cartEntity));
        when(mapper.mapToCartDto(cartEntity)).thenReturn(cartResponseDto);

        //when
        CartResponseDto returnValue = cartService.viewCartContent(userId);

        //Then
        assertNotNull(returnValue);
        assertEquals(cartResponseDto.getUserId(), returnValue.getUserId());
        verify(cartRepository).findByUserId(userId);
        verify(mapper).mapToCartDto(cartEntity);

    }

    @Test
    @DisplayName("clearCart_ReturnsSuccessMessage")
    final void testClearCart_ReturnsSuccessMessage() {

        //Response dto
        String message = "Cart cleared successfully";
        CartItem cartItem1 = new CartItem();
        cartItem1.setId(1L);

        CartItem cartItem2 = new CartItem();
        cartItem2.setId(2L);

        cartItemList.add(cartItem1);
        cartItemList.add(cartItem2);

        cartEntity = new Cart();
        cartEntity.setCartItems(cartItemList);

        User userEntity = new User();
        userEntity.setId(userId);
        userEntity.setCart(cartEntity);

        // Mocks
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(cartRepository.save(cartEntity)).thenReturn(cartEntity);

        // When
        String returnValue = cartService.clearCart(userId);

        //Then
        assertEquals(message, returnValue);
        verify(cartItemRepository).deleteById(1L);
        verify(cartItemRepository).deleteById(2L);
        verify(cartRepository).save(cartEntity);
        assertTrue(cartEntity.getCartItems().isEmpty());

    }


}
