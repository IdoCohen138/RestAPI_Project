package com.application.business;

import com.application.service.EnumStatus;
import com.application.utils.Client;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EndPointTest {

    RestTemplate restTemplate;
    String url;
    HttpHeaders headers;
    @LocalServerPort
    private int port;
    private Client myClient;
    private String id;
    private String status;

    private static Stream<Arguments> webhooks() throws IOException {
        InputStream inputStream = EndPointTest.class.getClassLoader().getResourceAsStream("config.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        String webhook_message_api = properties.getProperty("webhook_message_api");
        String webhook_message_api_2 = properties.getProperty("webhook_message_api_2");
        return Stream.of(
                Arguments.of(webhook_message_api,"starship"),
                Arguments.of(webhook_message_api_2,"starship2")
        );
    }

    @BeforeEach
    public void setup() {
        restTemplate = new RestTemplate();
        url = "http://localhost:" + port + "/channels";
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        myClient = new Client(url, headers, restTemplate);
        id= "/?id=";
        status= "/?status=";
    }

    @ParameterizedTest
    @MethodSource("webhooks")
    public void endToEndTestSuccess(String webhook,String channelName) {
        Assertions.assertEquals(myClient.post(jasonByParams(webhook, channelName)).getStatusCode(), HttpStatus.OK);
        String channelID=myClient.getIDbyWebhook(webhook);
        JSONObject requestBodyWithEnabledStatus = jasonByParams(channelID,EnumStatus.ENABLED);
        Assertions.assertEquals(myClient.put(requestBodyWithEnabledStatus).getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(myClient.getWithParmUrl(webhook, url + id + channelID).getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(myClient.getWithParmUrl(webhook, url + status + EnumStatus.ENABLED).getStatusCode(), HttpStatus.OK);
        JSONObject requestBodyWithDisablesStatus = jasonByParams(channelID,EnumStatus.DISABLED);
        Assertions.assertEquals(myClient.put(requestBodyWithDisablesStatus).getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(myClient.getWithParmUrl(webhook, url + status +EnumStatus.DISABLED).getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(myClient.getAllChannels().getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(myClient.delete(jasonByParams(channelID,channelName)).getStatusCode(), HttpStatus.OK);
        JSONObject nullID = jasonByParams("nullID",EnumStatus.DISABLED);

        Assertions.assertThrows(HttpClientErrorException.class, () -> {
            myClient.put(nullID);
        });
        Assertions.assertThrows(HttpClientErrorException.class, () -> {
            myClient.delete(nullID);
        });
    }


    private JSONObject jasonByParams(String webhook, String channelName, String id, EnumStatus enumStatus) {
        Map<String, Object> map = new HashMap<>();
        if (webhook != null) map.put("webhook", webhook);
        if (channelName != null) map.put("channelName", channelName);
        if (id != null) map.put("id", id);
        if (enumStatus != null) map.put("status", enumStatus);
        return new JSONObject(map);
    }
    private JSONObject jasonByParams(String webhook, String channelName) {
        return jasonByParams( webhook,channelName, null, null);
    }

    private JSONObject jasonByParams(String webhook, EnumStatus enumStatus) {
        return jasonByParams( null, null,webhook,enumStatus);
    }


}


