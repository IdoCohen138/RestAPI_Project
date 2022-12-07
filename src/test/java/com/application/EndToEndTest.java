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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EndToEndTest {
    RestTemplate restTemplate;
    String url;
    HttpHeaders headers;
    @LocalServerPort
    private int port;
    private Client myClient;
    String nullID = "{\"id\":\"NULLwebhhok\",\"channelName\":\"chanellname\"}";


    @BeforeEach
    public void setup() {
        restTemplate = new RestTemplate();
        url = "http://localhost:" + port + "/channels";
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        myClient = new Client(url, headers, restTemplate);
    }


    @ParameterizedTest
    @MethodSource("webhooks")
    public void postTest(String requestJson, String webhook) {
        Assertions.assertEquals(myClient.post(requestJson).getStatusCode(), HttpStatus.OK);
        myClient.delete("{\"id\":\"" + myClient.getIDsbyWebhook(webhook) + "\"}");

    }

    @ParameterizedTest
    @MethodSource("webhooks")
    public void putTest(String requestJson, String webhook) {
        myClient.post(requestJson);
        String body = "{\"id\":\"" + myClient.getIDsbyWebhook(webhook) + "\"}" + ",\"status\":\"ENABLED\"}";
        if (myClient.put(body).getStatusCode().equals(HttpStatus.OK)) {
            myClient.delete("{\"id\":\"" + myClient.getIDsbyWebhook(webhook) + "\"}");
        }
    }

    @Test
    public void putFailTest() {
        Assertions.assertThrows(HttpClientErrorException.class, () -> {
            myClient.put(nullID);
        });
    }

    @ParameterizedTest
    @MethodSource("webhooks")
    public void deleteTest(String requestJson, String webhook) {
        if (myClient.post(requestJson).getStatusCode().equals(HttpStatus.OK)) {
            Assertions.assertEquals(myClient.delete("{\"id\":\"" + myClient.getIDsbyWebhook(webhook) + "\"}").getStatusCode(), HttpStatus.OK);
        }
    }

    @Test
    public void deleteTestFail() {
        Assertions.assertThrows(HttpClientErrorException.class, () -> {
            myClient.delete(nullID);
        });

    }

    @ParameterizedTest
    @MethodSource("webhooks")
    public void getSpecificTest(String requestJson, String webhook, String Url) {
        if (myClient.post(requestJson).getStatusCode().equals(HttpStatus.OK)) {
            Assertions.assertEquals(myClient.getwithParmUrl(requestJson, url + Url + myClient.getIDsbyWebhook(webhook)).getStatusCode(), HttpStatus.OK);
            myClient.delete("{\"id\":\"" + myClient.getIDsbyWebhook(webhook) + "\"}");

        }
    }

    @ParameterizedTest
    @MethodSource("webhooks")
    public void getByStatusEnableDisableTest(String requestJson, String webhook, String Url, String status) {
        if (myClient.post(requestJson).getStatusCode().equals(HttpStatus.OK)) {
            Assertions.assertEquals(myClient.getwithParmUrl(requestJson, url + status + "ENABLED").getStatusCode(), HttpStatus.OK);
            String body = "{\"id\":\"" + myClient.getIDsbyWebhook(webhook) + "\"}" + ",\"status\":\"DISABLED\"}";
            myClient.put(body);
            Assertions.assertEquals(myClient.getwithParmUrl(requestJson, url + status + "DISABLED").getStatusCode(), HttpStatus.OK);
            myClient.delete("{\"id\":\"" + myClient.getIDsbyWebhook(webhook) + "\"}");
        }

    }

    @ParameterizedTest
    @MethodSource("webhooks")
    public void getAllChannels(String requestJson, String webhook) {

        myClient.post(requestJson);
        Assertions.assertEquals(myClient.getAllChannels().getStatusCode(), HttpStatus.OK);
        myClient.delete("{\"id\":\"" + myClient.getIDsbyWebhook(webhook) + "\"}");

    }


    private static Stream<Arguments> webhooks() {
        return Stream.of(
                Arguments.of("{\"webhook\":\"https://hooks.slack.com/services/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\",\"channelName\":\"liorchannel\"}", "https://hooks.slack.com/services/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", "/?id=", "/?status="),
                Arguments.of("{\"webhook\":\"https://hooks.slack.com/services/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\",\"channelName\":\"anotherone\"}", "https://hooks.slack.com/services/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", "/?id=", "/?status=")
        );
    }

}


