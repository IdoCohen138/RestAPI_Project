package com.application.utils;

import com.application.service.SlackChannel;
import lombok.AllArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;
@AllArgsConstructor()
public class Client {
    @NonNull
    String url;
    @NonNull
    HttpHeaders headers;
    @NonNull
    RestTemplate restTemplate;



    public ResponseEntity<String> post(JSONObject requestJson) {
        HttpEntity<JSONObject> entity = new HttpEntity<>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        return response;
    }

    public ResponseEntity<String> delete(JSONObject requestJson) {
        HttpEntity<JSONObject> entity = new HttpEntity<>(requestJson, headers);
        return restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);

    }

    public ResponseEntity<String> put(JSONObject requestJson) {
        HttpEntity<JSONObject> entity = new HttpEntity<>(requestJson, headers);
        return restTemplate.exchange(url,HttpMethod.PUT,entity,String.class);

    }

    public ResponseEntity<String> getWithParmUrl(String requestJson, String withParmUrl) {
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        return restTemplate.exchange(withParmUrl,HttpMethod.GET,entity,String.class);
    }

    public String getIDbyWebhook(String webhook) {
        HttpEntity<String> entity = new HttpEntity<>( headers);
        ResponseEntity<List<SlackChannel>> response =restTemplate.exchange(url,HttpMethod.GET,entity, new ParameterizedTypeReference<List<SlackChannel>>() {
        });
        List<SlackChannel> channels = response.getBody();
        for (SlackChannel channel : channels) {
            if (channel.getWebhook().equals(webhook))
                return channel.getId().toString();
        }
     return null;
    }
    public ResponseEntity<List<SlackChannel>> getAllChannels() {
        HttpEntity<String> entity = new HttpEntity<>( headers);
        return restTemplate.exchange(url,HttpMethod.GET,entity, new ParameterizedTypeReference<List<SlackChannel>>() {
        });

    }
}