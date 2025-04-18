package com.mustycodified.online_book_store.util;


import com.github.javafaker.Faker;
import com.mustycodified.online_book_store.entity.Book;
import com.mustycodified.online_book_store.entity.User;
import com.mustycodified.online_book_store.enums.Genre;
import com.mustycodified.online_book_store.repository.BookRepository;
import com.mustycodified.online_book_store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.mustycodified.online_book_store.enums.Roles.ADMIN;

@Component
@RequiredArgsConstructor
public class DummyData implements CommandLineRunner {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final Faker faker;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Book book = createBook();
            bookRepository.save(book);
        });
        System.out.println("preloading database with 10 books inventory data");
    }

    private Book createBook() {
        String rawIsbn = faker.code().isbn13();
        String[] genres = {
                Genre.FICTION.getGenre(),
                Genre.MYSTERY.getGenre(),
                Genre.THRILLER.getGenre(),
                Genre.POETRY.getGenre(),
                Genre.HORROR.getGenre(),
                Genre.SATIRE.getGenre()
        };

        return Book.builder()
                .title(faker.book().title())
                .isbn(rawIsbn.replaceAll("[^0-9-]", ""))
                .author(faker.book().author())
                .genre(genres[faker.number().numberBetween(0, genres.length)])
                .publishedYear(faker.number().numberBetween(1995, LocalDate.now().getYear()))
                .build();
    }


    @EventListener
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (!userRepository.existsByEmail("musty@gmail.com")) {
            System.out.println("Initializing Admin User");

            User admin = new User();
            admin.setUpdatedAt(LocalDateTime.now());
            admin.setCreatedAt(LocalDateTime.now());
            admin.setPassword(passwordEncoder.encode("0bv20S!ecQgd"));
            admin.setEmail("musty@gmail.com");
            admin.setRole(ADMIN.getPermissions().stream().map(Objects::toString).collect(Collectors.joining(",")));
            admin.setFirstName("Papi");
            admin.setLastName("Marciano");
            admin.setWalletBalance(BigDecimal.valueOf(500));
            userRepository.save(admin);
            System.out.println("Amin user created");

        } else {
            System.out.println("Amin user Already exists");
        }

    }

}
