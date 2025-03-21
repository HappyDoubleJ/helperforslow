package com.example.helperforslow.easyarticle.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GptApiClient {
    @Value("${openai.secret-key}")
    private String secretKey;

    @Value("${openai.model}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();

    public String sendArticleRequest(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model != null ? model : "gpt-3.5-turbo");
        requestBody.put("messages", List.of(Map.of("role", "user", "content", prompt)));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.openai.com/v1/chat/completions",
                entity,
                Map.class
        );

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        if (choices != null && !choices.isEmpty()) {
            Map<String, Object> firstChoice = choices.get(0);
            Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
            String rawResponse = (String) message.get("content");

            // ✅ JSON 정리
            if (rawResponse.startsWith("\"") && rawResponse.endsWith("\"")) {
                rawResponse = rawResponse.substring(1, rawResponse.length() - 1);
                rawResponse = rawResponse.replace("\\\"", "\"");
                rawResponse = rawResponse.replace("\\n", "");  // 개행 문자 제거
            }

            return rawResponse;
        }

        return "No response from OpenAI";
    }
}
