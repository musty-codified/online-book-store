package com.mustycodified.online_book_store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

@Data
public class ApiResponse<T> {
    private final boolean success;
    private final String message;
    private final String timestamp = new Timestamp(System.currentTimeMillis())
            .toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss+0000"));
    private final T data;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Wrapper<T> {
        private T content;
        private int totalPages;
        private long totalItems;
        private int totalItemsCurrentPage;
        private int currentPage;
        private boolean isLastPage;
        private boolean isFirstPage;
        private boolean isEmpty;
    }

}
