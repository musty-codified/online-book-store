package com.mustycodified.online_book_store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<com.mustycodified.online_book_store.entity.Book, Long> {
    @Query("SELECT b FROM Book b WHERE " +
            "(:searchText IS NULL OR " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(b.isbn) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(b.genre) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(b.author) LIKE LOWER(CONCAT('%', :searchText, '%')) )")
    Page<com.mustycodified.online_book_store.entity.Book> fetchAllBooks(
            @Param("searchText") String searchText,
            Pageable pageable);

}
