package com.mustycodified.online_book_store.impl;

import com.mustycodified.online_book_store.dto.CartItemsDto;
import com.mustycodified.online_book_store.dto.response.CartResponseDto;
import com.mustycodified.online_book_store.entity.Book;
import com.mustycodified.online_book_store.entity.Cart;
import com.mustycodified.online_book_store.entity.CartItem;
import com.mustycodified.online_book_store.entity.User;
import com.mustycodified.online_book_store.exception.ResourceNotFoundException;
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

    private User user;
    private Cart cart;
    private CartItemsDto cartItemsDto;
    private CartItemsDto expectedResponse;
    private CartItem cartItem;
    private List<CartItem> cartItemList = new ArrayList<>();
    private CartResponseDto cartResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);

        cart = new Cart();
        cart.setCartItems(cartItemList);
        user.setCart(cart);

        //Request DTO
        cartItemsDto = new CartItemsDto();
        cartItemsDto.setBookId(1L);
        cartItemsDto.setQuantity(5);

        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setBook(new Book());

        //Add to cart Response DTO
        expectedResponse = new CartItemsDto();
        cartItemsDto.setBookId(1L);
        cartItemsDto.setQuantity(5);

        //Response DTO
        cartResponseDto = new CartResponseDto();
        cartResponseDto.setUserId(user.getId());
        cartResponseDto.setCartItemsDto(new ArrayList<>());
    }

    @Test
    @DisplayName("addItemToCart_ReturnsCartItemDto")
    final void testAddItemToCart_ReturnsCartItemResponseDto() {

        // Mocks
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(mapper.mapToCartItems(cartItemsDto)).thenReturn(cartItem);
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(mapper.mapToCartItemsDto(any(CartItem.class))).thenReturn(expectedResponse);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        //when
        CartItemsDto returnValue = cartService.addItemToCart(user.getId(), cartItemsDto);

        //Then
        assertNotNull(returnValue);
        assertEquals(expectedResponse.getBookId(), returnValue.getBookId());
        verify(userRepository).findById(user.getId());
        verify(cartItemRepository).save(cartItem);
        verify(mapper).mapToCartItemsDto(any(CartItem.class));
        verify(cartItemRepository, times(1)).save(any(CartItem.class));

    }

    @Test
    @DisplayName("addItemToCart_ThrowsException")
    void testAddItemToCart_UserNotFound_ThrowsException() {
        // Given
        Long userId = 99L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                cartService.addItemToCart(userId, cartItemsDto));

        assertEquals("User Not Found", exception.getMessage());
        verify(userRepository).findById(userId);
        verifyNoInteractions(cartItemRepository);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("viewCartContent_ReturnsCartResponseDto")
    final void testViewCartContent_ReturnsCartResponseDto() {
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(mapper.mapToCartDto(cart)).thenReturn(cartResponseDto);

        CartResponseDto returnValue = cartService.viewCartContent(user.getId());

        assertNotNull(returnValue);
        assertEquals(cartResponseDto.getUserId(), returnValue.getUserId());
        verify(cartRepository).findByUserId(user.getId());
        verify(mapper).mapToCartDto(cart);

    }

    @Test
    @DisplayName("clearCart_ReturnsSuccessMessage")
    final void testClearCart_ReturnsSuccessMessage() {

        CartItem cartItem1 = new CartItem();
        cartItem1.setId(1L);

        CartItem cartItem2 = new CartItem();
        cartItem2.setId(2L);

        cartItemList.add(cartItem1);
        cartItemList.add(cartItem2);

        cart = new Cart();
        cart.setCartItems(cartItemList);

        // Mocks
        when(userRepository.findById(this.user.getId())).thenReturn(Optional.of(user));
        when(cartRepository.save(cart)).thenReturn(cart);

        // When
        String returnValue = cartService.clearCart(this.user.getId());

        //Then
        assertEquals("Cart cleared successfully", returnValue);
        verify(cartItemRepository).deleteById(1L);
        verify(cartItemRepository).deleteById(2L);
        verify(cartRepository).save(cart);
        assertTrue(cart.getCartItems().isEmpty());

    }


}
