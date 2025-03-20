package com.mustycodified.online_book_store.repository;

import com.mustycodified.online_book_store.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
