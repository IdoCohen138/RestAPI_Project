package com.application;

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

@SpringBootTest(webEnvironment =SpringBootTest.WebEnvironment.RANDOM_PORT)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EndToEndTest {
    RestTemplate restTemplate;
    String url;
    HttpHeaders headers;
    @LocalServerPort
    private int port;
    private Client myClient;
    String webhook="{\"webhook\":\"NULLwebhhok\",\"channelName\":\"chanellname\"}";


    @BeforeEach
    public void Setup() {
        restTemplate = new RestTemplate();
        url = "http://localhost:" + port + "/channels";
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        myClient=new Client(url,headers,restTemplate);
    }


    @ParameterizedTest
    @MethodSource("webhooks")
    public void PostTest(String requestJson) {
        Assertions.assertEquals(myClient.Post(requestJson).getStatusCode(),HttpStatus.OK);
        myClient.Delete(requestJson);

    }

    @ParameterizedTest
    @MethodSource("webhooks")
    public void PutTest(String requestJson) {
        myClient.Post(requestJson);
        if(myClient.Put(requestJson).getStatusCode().equals(HttpStatus.OK)) {
            myClient.Delete(requestJson);
        }
    }
@Test
    public void Put_fail_Test() {
        Assertions.assertThrows(HttpClientErrorException.class, () -> {
            myClient.Put(webhook);
        });
    }

    @ParameterizedTest
    @MethodSource("webhooks")
    public void DeleteTest(String requestJson) {
        if(myClient.Post(requestJson).getStatusCode().equals(HttpStatus.OK)) {
            Assertions.assertEquals(myClient.Delete(requestJson).getStatusCode(),HttpStatus.OK);
    }}
  @Test
    public void DeleteTest_fail() {
        Assertions.assertThrows(HttpClientErrorException.class, () -> {
            myClient.Delete(webhook);});

    }

    @ParameterizedTest
    @MethodSource("webhooks")
    public void GetSpecificTest(String requestJson,String Url) {
        if(myClient.Post(requestJson).getStatusCode().equals(HttpStatus.OK)) {
        Assertions.assertEquals(myClient.GetwithParmUrl(requestJson,url+Url).getStatusCode(),HttpStatus.OK);
            myClient.Delete(requestJson);

        }}

    @ParameterizedTest
    @MethodSource("webhooks")
    public void GetByStatus_enable_disable_Test(String requestJson,String channelname,String status) {
        if(myClient.Post(requestJson).getStatusCode().equals(HttpStatus.OK)) {
            Assertions.assertEquals(myClient.GetwithParmUrl(requestJson,url+status+"Enable").getStatusCode(),HttpStatus.OK);
            myClient.Put(requestJson);
            Assertions.assertEquals(myClient.GetwithParmUrl(requestJson,url+status+"Disable").getStatusCode(),HttpStatus.OK);
            myClient.Delete(requestJson);}

        }


    private static Stream<Arguments> webhooks() {
        return Stream.of(
                Arguments.of("{\"webhook\":\"https://hooks.slack.com/services/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\",\"channelName\":\"liorchannel\"}","?webhook=https://hooks.slack.com/services/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","?status=")
//                Arguments.of("{\"webhook\":\"Webhook_1\",\"channelName\":\"shanichannel\"}","?webhook=Webhook_1","?status="),
//                Arguments.of("{\"webhook\":\"Webhook_2\",\"channelName\":\"stam\"}","?webhook=Webhook_2","?status=")
        );
    }

}


