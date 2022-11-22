package com.example.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class MessageSender {
    private static final String HOOK_URL="https://hooks.slack.com/services/%s";
    private static final Map<String, String> USER_TO_CHANNEL_WEBHOOK =
            Map.of("Ido Cohen", "=T048XDR4ND6/B04BTKX8ENA/2Z3avYS4NJ7mqWbxzj9gufH9");
    public void sendMessage(String userName, Message message) throws JsonProcessingException {
        String userChannelId = USER_TO_CHANNEL_WEBHOOK.get(userName);
        String userWebhookUrl = String.format(HOOK_URL, userChannelId);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        String messageJson = objectMapper.writeValueAsString(message);

        HttpEntity<String> entity = new HttpEntity<>(messageJson, headers);
        restTemplate.exchange(userWebhookUrl, HttpMethod.POST, entity, String.class);

    }
}
