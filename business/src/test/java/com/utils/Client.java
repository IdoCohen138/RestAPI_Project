package com.utils;
import com.pack.SlackChannel;
import lombok.AllArgsConstructor;
import lombok.Setter;
import net.minidev.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor()
@Setter
public class Client {
    @NonNull
    private URI url;
    @NonNull
    private HttpHeaders headers;
    @NonNull
    private RestTemplate restTemplate;
    private URI urlWithID;
    private URI urlWithStatus;


    public ResponseEntity<String> post(JSONObject requestJson) {
        HttpEntity<JSONObject> entity = new HttpEntity<>(requestJson, headers);
        return restTemplate.postForEntity(url, entity, String.class);
    }

    public ResponseEntity<String> delete() {
        HttpEntity<JSONObject> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(urlWithID, HttpMethod.DELETE, entity, String.class);

    }

    public ResponseEntity<String> put(JSONObject requestJson) {
        HttpEntity<JSONObject> entity = new HttpEntity<>(requestJson, headers);
        return restTemplate.exchange(urlWithID, HttpMethod.PUT, entity, String.class);

    }

    public ResponseEntity<SlackChannel> getSpecificChannel() {
        HttpEntity<SlackChannel> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(urlWithID, HttpMethod.GET, entity, SlackChannel.class);
    }

    public UUID getIDbyWebhook(String webhook) {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<List<SlackChannel>> response = restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<SlackChannel>>() {
        });
        List<SlackChannel> channels = response.getBody();
        for (SlackChannel channel : channels) {
            if (channel.getWebhook().equals(webhook))
                return channel.getId();
        }
        return null;
    }


    public ResponseEntity<List<SlackChannel>> getAllChannels() {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(this.url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<SlackChannel>>() {
        });

    }

    public ResponseEntity<List<SlackChannel>> getAllChannelsWithStatus() {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(urlWithStatus, HttpMethod.GET, entity, new ParameterizedTypeReference<List<SlackChannel>>() {
        });

    }
    public void deleteAll(UUID id) {
        HttpEntity<JSONObject> entity = new HttpEntity<>(headers);
        UriComponents uriComponent = UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(8080).path("channels/"+id).build();
        restTemplate.exchange(uriComponent.toUri(), HttpMethod.DELETE, entity, String.class);

    }
}