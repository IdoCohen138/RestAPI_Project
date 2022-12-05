package com.application;

import com.application.presentationLayer.DataAccess;
import com.application.serviceLayer.EnumStatus;
import com.application.serviceLayer.Exceptions.SlackMessageNotSentException;
import com.application.serviceLayer.SlackChannel;
import com.application.serviceLayer.SlackIntegration;
import com.slack.api.Slack;
import com.slack.api.methods.response.oauth.OAuthTokenResponse;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class SendMessageIntegrationTest {
    Slack slack;
    SlackIntegration slackIntegration;
    static Properties properties;
    SlackChannel slackChannel;
    HttpHeaders headers;


    @BeforeEach
    public void setUp() throws IOException {

        slack = Mockito.mock(Slack.class);

        //read from properties
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
        properties = new Properties();
        properties.load(inputStream);
        String webhook = properties.getProperty("webhook_message_api");

        //set new channel
        slackChannel = new SlackChannel();
        slackChannel.setId(UUID.randomUUID());
        slackChannel.setWebhook(webhook);
        slackChannel.setStatus(EnumStatus.ENABLED);


        slackIntegration = new SlackIntegration();
        ReflectionTestUtils.setField(slackIntegration,"slack",slack);
    }
    @ParameterizedTest
    @MethodSource("webhooks")
    public void sendMessageTest(String requestJson,String webhook) throws SlackMessageNotSentException, IOException {

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> httpResponse = new HttpEntity<>(requestJson, headers);
        WebhookResponse webhookResponse = WebhookResponse.builder().code(200).message("ok").headers(httpResponse.getHeaders()).body("body").build();

        when(slack.send(any(), (Payload) any())).thenReturn(webhookResponse);

        WebhookResponse webhookResponseToTest = slackIntegration.sendMessage(slackChannel,"You have no vulnerabilities");

        assertEquals(webhookResponseToTest.getCode(),200);
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
