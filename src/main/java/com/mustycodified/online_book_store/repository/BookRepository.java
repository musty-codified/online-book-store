package com.mustycodified.online_book_store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<com.mustycodified.online_book_store.entity.Book, Long> {
    @Query("SELECT b FROM Book b WHERE " +
            "(:searchText IS NULL OR " +
            "LOWER(b.title) ILIKE CONCAT('%', :searchText, '%') OR " +
            "LOWER(b.isbn) ILIKE CONCAT('%', :searchText, '%') OR " +
            "LOWER(b.genre) ILIKE CONCAT('%', :searchText, '%') OR " +
            "LOWER(b.author) ILIKE CONCAT('%', :searchText, '%') )")
    Page<com.mustycodified.online_book_store.entity.Book> fetchAllBooks(
            @Param("searchText") String searchText,
            Pageable pageable);

}
