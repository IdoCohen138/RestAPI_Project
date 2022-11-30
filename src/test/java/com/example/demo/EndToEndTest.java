package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EndToEndTest {
    RestTemplate restTemplate;
    String url;
    HttpHeaders headers;
    @LocalServerPort
    private int port;

    @BeforeEach
    public void Setup() {
        restTemplate = new RestTemplate();
        url = "http://localhost:" + port + "/channels";
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    private static Stream<Arguments> webhooks() {
        return Stream.of(
                Arguments.of("{\"webhook\":\"https://hooks.slack.com/services/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\",\"channelName\":\"liorchannel\"}","?webhook=https://hooks.slack.com/services/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","?status="),
                Arguments.of("{\"webhook\":\"Webhook_1\",\"channelName\":\"shanichannel\"}","?webhook=Webhook_1","?status="),
                Arguments.of("{\"webhook\":\"Webhook_2\",\"channelName\":\"stam\"}","?webhook=Webhook_2","?status=")
        );
    }

    @ParameterizedTest
    @MethodSource("webhooks")
    public void Post_success_Test(String requestJson) {
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        Assertions.assertEquals(response.getStatusCode(),HttpStatus.OK);
        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);

    }

    @ParameterizedTest
    @MethodSource("webhooks")
    public void PutTest(String requestJson) {
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        if(response.getStatusCode().equals(HttpStatus.OK)) {
            this.restTemplate.put(url, entity);
            restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
        }
    }
@Test
    public void Put_fail_Test() {
        String webhook="{\"webhook\":\"NULLwebhhok\",\"channelName\":\"chanellname\"}";
        HttpEntity<String> entity = new HttpEntity<>(webhook, headers);

    Assertions.assertThrows(HttpClientErrorException.class, () -> {
        restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
    });
    }

    @ParameterizedTest
    @MethodSource("webhooks")
    public void DeleteTest(String requestJson) {
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        if(response.getStatusCode().equals(HttpStatus.OK)) {
            ResponseEntity<Void> resp = restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
            Assertions.assertEquals(resp.getStatusCode(),HttpStatus.OK);
        }

    }
    public void DeleteTest_fail() {
        String webhook="{\"webhook\":\"NULLwebhhok\",\"channelName\":\"chanellname\"}";
        HttpEntity<String> entity = new HttpEntity<>(webhook, headers);
        Assertions.assertThrows(HttpClientErrorException.class, () -> {
            restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);});

    }

    @ParameterizedTest
    @MethodSource("webhooks")
    public void GetSpecificTest(String requestJson,String Url) {
        HttpEntity<String> entity= new HttpEntity<>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        if(response.getStatusCode().equals(HttpStatus.OK)) {
        String withParmUrl=url+Url;
        ResponseEntity<String> responseEntity=this.restTemplate.exchange(withParmUrl,HttpMethod.GET,entity,String.class);
        Assertions.assertEquals(responseEntity.getStatusCode(),HttpStatus.OK);
        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }}

    @ParameterizedTest
    @MethodSource("webhooks")
    public void GetByStatus_enable_disable_Test(String requestJson,String channelname,String status) {
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        if(response.getStatusCode().equals(HttpStatus.OK)) {
            String withParmUrl=url+status+"Enable";
            ResponseEntity<String> responseEntity=this.restTemplate.exchange(withParmUrl,HttpMethod.GET,entity,String.class);
            Assertions.assertEquals(responseEntity.getStatusCode(),HttpStatus.OK);

            restTemplate.put(url, entity, String.class);
            withParmUrl=url+status+"Disable";
            responseEntity=this.restTemplate.exchange(withParmUrl,HttpMethod.GET,entity,String.class);
            Assertions.assertEquals(responseEntity.getStatusCode(),HttpStatus.OK);

            restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }}

}
