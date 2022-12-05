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

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment =SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EndToEndTest {

    RestTemplate restTemplate;
    String url;
    HttpHeaders headers;
    @LocalServerPort
    private int port;
    private Client myClient;
    String nullID="{\"id\":\"NULLwebhhok\",\"channelName\":\"chanellname\"}";
    private static Properties properties;


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
    public void PostTest(String requestJson,String webhook) {
        Assertions.assertEquals(myClient.Post(requestJson).getStatusCode(),HttpStatus.OK);
        myClient.Delete("{\"id\":\""+myClient.GetIDsbyWebhook(webhook)+"\"}");

    }

    @ParameterizedTest
    @MethodSource("webhooks")
    public void PutTest(String requestJson,String webhook) {
        myClient.Post(requestJson);
        String body="{\"id\":\""+myClient.GetIDsbyWebhook(webhook)+"\"}"+",\"status\":\"ENABLED\"}";
        if(myClient.Put(body).getStatusCode().equals(HttpStatus.OK)) {
            myClient.Delete("{\"id\":\""+myClient.GetIDsbyWebhook(webhook)+"\"}");
        }
    }
@Test
    public void Put_fail_Test() {
        Assertions.assertThrows(HttpClientErrorException.class, () -> {
            myClient.Put(nullID);
        });
    }

    @ParameterizedTest
    @MethodSource("webhooks")
    public void DeleteTest(String requestJson,String webhook) {
        if(myClient.Post(requestJson).getStatusCode().equals(HttpStatus.OK)) {
            Assertions.assertEquals(myClient.Delete("{\"id\":\""+myClient.GetIDsbyWebhook(webhook)+"\"}").getStatusCode(),HttpStatus.OK);
    }}

  @Test
    public void DeleteTest_fail() {
        Assertions.assertThrows(HttpClientErrorException.class, () -> {
            myClient.Delete(nullID);});

    }

    @ParameterizedTest
    @MethodSource("webhooks")
    public void GetSpecificTest(String requestJson,String webhook,String Url) {
        if(myClient.Post(requestJson).getStatusCode().equals(HttpStatus.OK)) {
        Assertions.assertEquals(myClient.GetwithParmUrl(requestJson,url+Url+myClient.GetIDsbyWebhook(webhook)).getStatusCode(),HttpStatus.OK);
            myClient.Delete("{\"id\":\""+myClient.GetIDsbyWebhook(webhook)+"\"}");

        }}

    @ParameterizedTest
    @MethodSource("webhooks")
    public void GetByStatus_enable_disable_Test(String requestJson,String webhook,String Url,String status) {
        if(myClient.Post(requestJson).getStatusCode().equals(HttpStatus.OK)) {
            Assertions.assertEquals(myClient.GetwithParmUrl(requestJson,url+status+"ENABLED").getStatusCode(),HttpStatus.OK);
            String body="{\"id\":\""+myClient.GetIDsbyWebhook(webhook)+"\"}"+",\"status\":\"DISABLED\"}";
            myClient.Put(body);
            Assertions.assertEquals(myClient.GetwithParmUrl(requestJson,url+status+"DISABLED").getStatusCode(),HttpStatus.OK);
            myClient.Delete("{\"id\":\""+myClient.GetIDsbyWebhook(webhook)+"\"}");}

        }
    @ParameterizedTest
    @MethodSource("webhooks")
    public void GetAllChannels(String requestJson,String webhook) {

        myClient.Post(requestJson);
        Assertions.assertEquals(myClient.GetAllChannels().getStatusCode(),HttpStatus.OK);
        myClient.Delete("{\"id\":\""+myClient.GetIDsbyWebhook(webhook)+"\"}");

        }


    private static Stream<Arguments> webhooks() throws IOException {
        InputStream inputStream = EndToEndTest.class.getClassLoader().getResourceAsStream("config.properties");
        properties = new Properties();
        properties.load(inputStream);
        String webhook_message_api = properties.getProperty("webhook_message_api");
        String webhook_message_api_2 = properties.getProperty("webhook_message_api_2");
        return Stream.of(
                Arguments.of(String.format("{\"webhook\":\"%s\",\"channelName\":\"liorchannel\"}", webhook_message_api),webhook_message_api,"/?id=","/?status="),
                Arguments.of(String.format("{\"webhook\":\"%s\",\"channelName\":\"anotherone\"}",webhook_message_api_2),webhook_message_api_2,"/?id=","/?status=")
        );
    }

}


