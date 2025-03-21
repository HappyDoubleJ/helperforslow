package com.example.helperforslow.easyarticle.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;


@Configuration
public class GptConfig {
    @Value("${openai.secret-key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        System.out.println("🔑 OpenAI Secret Key: " + secretKey);
    }
}
