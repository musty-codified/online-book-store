package com.mustycodified.online_book_store.config;

import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class FakerConfig {

    @Bean
    public Faker faker(){
        return new Faker();
    }
}
