package com.application;

import com.application.service.SlackChannel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.UUID;

public class Client {
    String url;
    HttpHeaders headers;
    RestTemplate restTemplate;

    public Client(String urll, HttpHeaders headerss, RestTemplate restTemplatee) {
        url=urll;
        headers=headerss;
        restTemplate=restTemplatee;
    }


    public ResponseEntity<String> Post(String requestJson) {
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        return response;
    }

    public ResponseEntity<String> Delete(String requestJson) {
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        return restTemplate.<String>exchange(url, HttpMethod.DELETE, entity, String.class);

    }

    public ResponseEntity<String> Put(String requestJson) {
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        return restTemplate.exchange(url,HttpMethod.PUT,entity,String.class);

    }

    public ResponseEntity<String> getwithParmUrl(String requestJson, String withParmUrl) {
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        return restTemplate.exchange(withParmUrl,HttpMethod.GET,entity,String.class);
    }

    public UUID getIDsbyWebhook(String webhook) {
        HttpEntity<String> entity = new HttpEntity<>( headers);
        ResponseEntity<ArrayList<SlackChannel>> response =restTemplate.exchange(url,HttpMethod.GET,entity, new ParameterizedTypeReference<ArrayList<SlackChannel>>() {
        });
        ArrayList<SlackChannel> channels = response.getBody();
        for (int i=0;i<channels.size();i++){
            if(channels.get(i).getWebhook().equals(webhook))
                return channels.get(i).getId();
        }
     return null;
    }
    public ResponseEntity<ArrayList<?>> getAllChannels() {
        HttpEntity<String> entity = new HttpEntity<>( headers);
        return restTemplate.exchange(url,HttpMethod.GET,entity, new ParameterizedTypeReference<ArrayList<?>>() {
        });

    }
}