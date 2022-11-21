package com.example.demo;

import com.example.demo.controllers.SlackChannelController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;
import java.util.Map;


public class integrationTest {
    private static HttpClient client;
    private static Client myclient;
    private static SlackChannelController slackChannelController;


    @BeforeAll
    static void setUp() {

        client = HttpClient.newHttpClient();
        slackChannelController=new SlackChannelController();
        myclient=new Client(slackChannelController);
    }

    @Test
    void createChannelTest() throws IOException, InterruptedException {
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("http://webcode.me"))
//                .GET()
//                .build();
//
//        HttpResponse<Void> response = client.send(request,
//            HttpResponse.BodyHandlers.discarding());
        Map<String,String> json=new LinkedHashMap<>();
        json.put("firstchannel","https://hooks.slack.com/services/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");
        Assertions.assertTrue(myclient.CreateChannelTest(json));
    //    System.out.println(response.statusCode());
    }




}