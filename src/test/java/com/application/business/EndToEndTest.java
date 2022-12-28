package com.application.business;

import com.application.service.EnumStatus;
import com.application.service.SlackChannel;
import com.application.utils.Client;
import lombok.SneakyThrows;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Stream;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class EndToEndTest {

    RestTemplate restTemplate;
    URL url;
    HttpHeaders headers;
    List<SlackChannel> array;
    SlackChannel slackChannel;
    UriComponents uriComponentsWithID;
    UriComponents uriComponentsWithStatus;
    private Client myClient;

    private static Stream<Arguments> webhooks() throws IOException {
        InputStream inputStream = EndToEndTest.class.getClassLoader().getResourceAsStream("config.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        String webhook_message_api = properties.getProperty("webhook_message_api");
        String webhook_message_api_2 = properties.getProperty("webhook_message_api_2");
        return Stream.of(
                Arguments.of(webhook_message_api, "starship"),
                Arguments.of(webhook_message_api_2, "starship2")
        );
    }

    @SneakyThrows
    @BeforeEach
    public void setup() {
        restTemplate = new RestTemplate();
        url = new URL("http://localhost:8080/channels");
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        array=new ArrayList<SlackChannel>();
        uriComponentsWithID = UriComponentsBuilder.newInstance()
                .scheme("http").host("localhost").port(8080).path("channels/{id}").build();
        uriComponentsWithStatus = UriComponentsBuilder.newInstance()
                .scheme("http").host("localhost").port(8080)
                .path("channels/").query("status={status}").build();
        myClient = new Client(url.toURI(), headers, restTemplate, uriComponentsWithID.toUri(), uriComponentsWithStatus.toUri());

    }

    @ParameterizedTest
    @MethodSource("webhooks")
    public void endToEndTestSuccess(String webhook, String channelName) throws IOException {
        checkForEmptySlackChannelTable();
        createAndPostSlackChannel(webhook,channelName);
        updateStatusDisabled();
        getOneChannelByStatus();
        getALlChannelsByStatusDisabled_oneChannel();
        getALlChannelsByStatusEnabled_emptyList();
        deleteAndCheckEmptyList();

    }

    private void deleteAndCheckEmptyList() {
        Assertions.assertEquals(myClient.delete().getStatusCode(), HttpStatus.OK);
        array.remove(slackChannel);
        Assertions.assertEquals(myClient.getAllChannels().getBody(), array);

    }

    private void getALlChannelsByStatusEnabled_emptyList() {
        myClient.setUrlWithStatus(uriComponentsWithStatus.expand(EnumStatus.ENABLED.toString()).toUri());
        Assertions.assertEquals(myClient.getAllChannelsWithStatus().getBody(), new ArrayList<>());

    }


    private void getALlChannelsByStatusDisabled_oneChannel() {
        myClient.setUrlWithStatus(uriComponentsWithStatus.expand(EnumStatus.DISABLED.toString()).toUri());
        Assertions.assertEquals(myClient.getAllChannelsWithStatus().getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(myClient.getAllChannelsWithStatus().getBody(), array);

    }

    private void getOneChannelByStatus() {
        Assertions.assertEquals(myClient.getSpecificChannel().getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(myClient.getSpecificChannel().getBody().getStatus(), slackChannel.getStatus());

    }

    private void updateStatusDisabled() {
        slackChannel.setStatus(EnumStatus.DISABLED);
        JSONObject requestBodyWithDisabledStatus = jasonByParams(EnumStatus.DISABLED);
        Assertions.assertEquals(myClient.put(requestBodyWithDisabledStatus).getStatusCode(), HttpStatus.OK);
    }

    private void createAndPostSlackChannel(String webhook,String channelName) throws IOException {
        Assertions.assertEquals(myClient.post(jasonByParams(webhook, channelName)).getStatusCode(), HttpStatus.OK);
        UUID channelID = myClient.getIDbyWebhook(webhook);
        myClient.setUrlWithID(uriComponentsWithID.expand(channelID.toString()).toUri());
        createSlackChannel(channelID, webhook);
        array.add(slackChannel);
        Assertions.assertEquals(myClient.getAllChannels().getBody(), array);

    }

    private void checkForEmptySlackChannelTable() {
        Assertions.assertEquals(myClient.getAllChannels().getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(myClient.getAllChannels().getBody(), array);
    }


    private JSONObject jasonByParams(String webhook, String channelName, EnumStatus enumStatus) {
        Map<String, Object> map = new HashMap<>();
        if (webhook != null) map.put("webhook", webhook);
        if (channelName != null) map.put("channelName", channelName);
        if (enumStatus != null) map.put("status", enumStatus);
        return new JSONObject(map);
    }

    private JSONObject jasonByParams(String webhook, String channelName) {
        return jasonByParams(webhook, channelName, null);
    }

    private JSONObject jasonByParams(EnumStatus enumStatus) {
        return jasonByParams(null, null, enumStatus);
    }

    private void createSlackChannel(UUID id, String webhook) throws IOException {
        slackChannel = new SlackChannel();
        slackChannel.setId(id);
        slackChannel.setWebhook(webhook);
        slackChannel.setStatus(EnumStatus.ENABLED);

    }

}


