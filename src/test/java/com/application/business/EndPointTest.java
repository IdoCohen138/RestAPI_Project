package com.application.business;

import com.application.service.EnumStatus;
import com.application.utils.Client;
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
import java.util.Collections;
import java.util.Properties;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EndPointTest {

    RestTemplate restTemplate;
    String url;
    HttpHeaders headers;
    String nullID = "{\"id\":\"NULLwebhhok\",\"channelName\":\"chanellname\"}";
    @LocalServerPort
    private int port;
    private Client myClient;

    private static Stream<Arguments> webhooks() throws IOException {
        InputStream inputStream = EndPointTest.class.getClassLoader().getResourceAsStream("config.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        String webhook_message_api = properties.getProperty("webhook_message_api");
        String webhook_message_api_2 = properties.getProperty("webhook_message_api_2");
        return Stream.of(
                Arguments.of(String.format("{\"webhook\":\"%s\",\"channelName\":\"liorchannel\"}", webhook_message_api), webhook_message_api, "/?id=", "/?status="),
                Arguments.of(String.format("{\"webhook\":\"%s\",\"channelName\":\"anotherone\"}", webhook_message_api_2), webhook_message_api_2, "/?id=", "/?status=")
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
    }

    @ParameterizedTest
    @MethodSource("webhooks")
    public void EndToEndTestSuccess(String requestJson, String webhook, String Url, String status) {
        Assertions.assertEquals(myClient.Post(requestJson).getStatusCode(), HttpStatus.OK);
        String body_E = "{\"id\":\"" + myClient.getIDsbyWebhook(webhook) + "\"}" + ",\"status\":" + EnumStatus.ENABLED + "}";
        Assertions.assertEquals(myClient.Put(body_E).getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(myClient.getwithParmUrl(requestJson, url + Url + myClient.getIDsbyWebhook(webhook)).getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(myClient.getwithParmUrl(requestJson, url + status + EnumStatus.ENABLED).getStatusCode(), HttpStatus.OK);
        String body_D = "{\"id\":\"" + myClient.getIDsbyWebhook(webhook) + "\"}" + ",\"status\":" + EnumStatus.DISABLED + "}";
        Assertions.assertEquals(myClient.Put(body_D).getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(myClient.getwithParmUrl(requestJson, url + status +EnumStatus.DISABLED).getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(myClient.getAllChannels().getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(myClient.Delete("{\"id\":\"" + myClient.getIDsbyWebhook(webhook) + "\"}").getStatusCode(), HttpStatus.OK);
        Assertions.assertThrows(HttpClientErrorException.class, () -> {
            myClient.Put(nullID);
        });
        Assertions.assertThrows(HttpClientErrorException.class, () -> {
            myClient.Delete(nullID);
        });
    }

}


