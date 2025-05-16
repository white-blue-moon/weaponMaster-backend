package com.example.weaponMaster.config;

import com.example.weaponMaster.config.util.LoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder
                .requestInterceptor(new LoggingInterceptor())
                .build();
    }
}
