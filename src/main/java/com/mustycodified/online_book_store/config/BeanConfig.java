package com.mustycodified.online_book_store.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BeanConfig {

    @Bean
    public Faker faker(){
        return new Faker();
    }

    @Bean
    public ObjectMapper mapper(){
        return new ObjectMapper();
    }

}
