package com.mustycodified.online_book_store.repository;

import com.mustycodified.online_book_store.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
