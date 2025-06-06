package com.mustycodified.online_book_store.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "access-control-list")
@EnableConfigurationProperties
public class PermissionConfig {
    private List<PermissionMapping> permissions = new ArrayList<>();

}
