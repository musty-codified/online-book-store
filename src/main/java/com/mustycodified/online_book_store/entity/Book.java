package com.mustycodified.online_book_store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "books")
public class Book extends Base {

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 100)
    private String genre;

    @Column(nullable = false, length = 50)
    private String author;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^[0-9-]+$", message = "ISBN must contain only digits and dashes")
    private String isbn;

    @Column(nullable = false)
    private Integer publishedYear;

}
