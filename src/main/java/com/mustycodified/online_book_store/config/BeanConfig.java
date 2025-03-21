package com.mustycodified.online_book_store.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

@Component
@Configuration
public class BeanConfig {

    @Bean
    public Faker faker(){
        return new Faker();
    }

    @Bean
    public ObjectMapper mapper(){
        return new ObjectMapper();
    }

    @Bean(name = "asyncTaskExecutor")
    public Executor asyncTaskExecutor(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(4);
        taskExecutor.setQueueCapacity(100);
        taskExecutor.setMaxPoolSize(4);
        taskExecutor.setThreadNamePrefix("paymentThread-");
        taskExecutor.initialize();
        return taskExecutor;
    }

}
