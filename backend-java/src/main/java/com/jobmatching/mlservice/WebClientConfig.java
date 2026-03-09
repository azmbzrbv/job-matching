package com.jobmatching.mlservice;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient mlWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8000") // The address of your Python FastAPI
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024)) // Allows files up to 16MB
                .build();
    }
}