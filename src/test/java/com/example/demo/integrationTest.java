package com.example.demo;

import com.example.demo.businessLayer.SlackChannelController;
import com.example.demo.serviceLayer.SlackChannel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.http.HttpClient;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class integrationTest {
    private static HttpClient client;
    private static Client myclient;
    private static SlackChannelController slackChannelController;
    private int chanells_size=0;


    @BeforeAll
    static void setUp() {

        client = HttpClient.newHttpClient();
        slackChannelController=new SlackChannelController();
        myclient=new Client(slackChannelController);
    }

    @Test
    void createChannelTest() throws IOException, InterruptedException, NoSuchFieldException, IllegalAccessException {
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


        SlackChannelController slackChannelController = new SlackChannelController();
        Field channels = slackChannelController.getClass().getDeclaredField("channels");
        channels.setAccessible(true);
        List<SlackChannel> channels_ = (List<SlackChannel>) channels.get(slackChannelController);

        assertEquals(chanells_size,channels_.size());

    }



}