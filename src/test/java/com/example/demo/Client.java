package com.example.demo;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

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

    public ResponseEntity<String> GetwithParmUrl(String requestJson, String withParmUrl) {
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        return restTemplate.exchange(withParmUrl,HttpMethod.GET,entity,String.class);
    }

}