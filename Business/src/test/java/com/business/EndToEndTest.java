package com.business;


import com.pack.*;
import org.junit.jupiter.api.AfterAll;
import org.springframework.http.*;
import com.utils.Client;
import lombok.SneakyThrows;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.stream.Stream;

public class EndToEndTest {

    RestTemplate restTemplate;
    URL url;
    HttpHeaders headers;
    List<SlackChannel> array;
    SlackChannel slackChannel;
    UriComponents uriComponentsWithID;
    UriComponents uriComponentsWithStatus;
    private Client myClient;

    private static String docker_compose_path;

    private static Stream<Arguments> webhooks() throws IOException {
        InputStream inputStream = EndToEndTest.class.getClassLoader().getResourceAsStream("config.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        docker_compose_path=properties.getProperty("docker_compose_path");
        String webhook_message_api = properties.getProperty("webhook_message_api");
        String webhook_message_api_2 = properties.getProperty("webhook_message_api_2");
        return Stream.of(Arguments.of(webhook_message_api, "starship"), Arguments.of(webhook_message_api_2, "starship2"));
    }

    @SneakyThrows
    @BeforeEach
    public void setup() {
        runDockerComposeUp();
        restTemplate = new RestTemplate();
        url = new URL("http://localhost:8080/channels");
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        array = new ArrayList<SlackChannel>();
        uriComponentsWithID = UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(8080).path("channels/{id}").build();
        uriComponentsWithStatus = UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(8080).path("channels/").query("status={status}").build();
        myClient = new Client(url.toURI(), headers, restTemplate, uriComponentsWithID.toUri(), uriComponentsWithStatus.toUri());
        waitFor() ;
        deleteAllDataBase();

    }

    @AfterAll
    public static void setDown() throws IOException {
        String[] commandStop = {"cmd", "/c", "docker-compose", "-f", "docker-compose.yaml", "down"};
        ProcessBuilder builderStop = new ProcessBuilder(commandStop);
        builderStop.directory(new File(docker_compose_path));
        Process processStop = builderStop.start();
        BufferedReader readerStop = new BufferedReader(new InputStreamReader(processStop.getInputStream()));
        String lineStop;
        while ((lineStop = readerStop.readLine()) != null) {
            System.out.println(lineStop);
        }
        readerStop.close();
    }




    public  void runDockerComposeUp() throws IOException, InterruptedException {
        String[] command = {"cmd", "/c", "docker-compose", "-f", "docker-compose.yml", "up", "-d"};
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(new File(docker_compose_path));
        Process process = builder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

    }



    @ParameterizedTest
    @MethodSource("webhooks")
    public void endToEndTestSuccess(String webhook, String channelName) throws IOException, InterruptedException {
        checkForEmptySlackChannelTable();
        createAndPostSlackChannel(webhook, channelName);
        updateStatusDisabled();
        getOneChannelByStatus();
        getALlChannelsByStatusDisabled_returns_one_channel();
        getALlChannelsByStatusEnabled_returns_empty_list();
        deleteAndCheckEmptyList();

    }

    private void deleteAndCheckEmptyList() {
        Assertions.assertEquals(myClient.delete().getStatusCode(), HttpStatus.OK);
        array.remove(slackChannel);
        Assertions.assertEquals(myClient.getAllChannels().getBody(), array);

    }

    private void getALlChannelsByStatusEnabled_returns_empty_list() {
        myClient.setUrlWithStatus(uriComponentsWithStatus.expand(EnumStatus.ENABLED.toString()).toUri());
        Assertions.assertEquals(myClient.getAllChannelsWithStatus().getBody(), new ArrayList<>());

    }


    private void getALlChannelsByStatusDisabled_returns_one_channel() {
        myClient.setUrlWithStatus(uriComponentsWithStatus.expand(EnumStatus.DISABLED.toString()).toUri());
        Assertions.assertEquals(myClient.getAllChannelsWithStatus().getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(myClient.getAllChannelsWithStatus().getBody(), array);

    }

    private void getOneChannelByStatus() {
        Assertions.assertEquals(myClient.getSpecificChannel().getBody().getStatus(), slackChannel.getStatus());

    }

    private void updateStatusDisabled() {
        slackChannel.setStatus(EnumStatus.DISABLED);
        JSONObject requestBodyWithDisabledStatus = jasonByParams(EnumStatus.DISABLED);
        Assertions.assertEquals(myClient.put(requestBodyWithDisabledStatus).getStatusCode(), HttpStatus.OK);
    }

    private void createAndPostSlackChannel(String webhook, String channelName) throws IOException {
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
    private void deleteAllDataBase() {
        List<SlackChannel> arraylist=myClient.getAllChannels().getBody();
        for (int i=0;i<arraylist.size();i++){
            myClient.deleteAll(arraylist.get(i).getId());
        }
    }
    private void waitFor() throws InterruptedException {
        boolean isready=false;
        while (!isready){
            try {
                myClient.getAllChannels().getBody();
            } catch (Exception  e) {
                Thread.sleep(1000);
                continue;
            }
            isready=true;}

    }

}


