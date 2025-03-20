package com.mustycodified.online_book_store.util;


import com.github.javafaker.Faker;
import com.mustycodified.online_book_store.entity.Book;
import com.mustycodified.online_book_store.entity.User;
import com.mustycodified.online_book_store.repository.BookRepository;
import com.mustycodified.online_book_store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private final AppUtil appUtil;
    private final Faker faker;
    @Override
    public void run(String... args) throws Exception {
        IntStream.rangeClosed(1, 10).forEach(i->{
            Book book = createBook();
            bookRepository.save(book);
        });
        System.out.println("Seeding dummy book data");
    }


    private Book createBook(){
        return Book.builder()
                .title(faker.book().title())
                .isbn(faker.number().digits(13))
                .author(faker.book().author())
                .genre(faker.book().genre())
                .publishedYear(generateRandomYear())
                .quantity(faker.random().nextInt(100))
                .price(generateRandomPrice())
                .build();
    }


    @EventListener
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event){
        if (!userRepository.existsByEmail("musty@gmail.com")){
            System.out.println("Initializing Admin User");

            User admin = new User();
            admin.setUpdatedAt(LocalDateTime.now());
            admin.setCreatedAt(LocalDateTime.now());
            admin.setPassword(("0bv20S!ecQgd"));
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

    private int generateRandomYear() {
        return faker.number().numberBetween(2015, 2025);
    }
    private BigDecimal generateRandomPrice() {
        double price = 10 + (90 * faker.random().nextDouble());
        return BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
    }



}
