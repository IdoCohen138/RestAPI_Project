package com.application;

import com.application.service.EnumStatus;
import com.application.service.exceptions.SlackMessageNotSentException;
import com.application.service.SlackChannel;
import com.application.service.SlackIntegration;
import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestPropertySource("/config.properties")
public class SendMessageIntegrationTest {
    Slack slack;
    SlackIntegration slackIntegration;
    SlackChannel slackChannel;
    HttpHeaders headers;
    @Value("${webhook_message_api}") static String webhook_message_api_property;
    @Value("${webhook_message_api}") static String webhook_message_api_2_property;

    @BeforeEach
    public void setup() throws IOException {

        slack = Mockito.mock(Slack.class);

        //set new channel
        slackChannel = new SlackChannel();
        slackChannel.setId(UUID.randomUUID());
        slackChannel.setWebhook(webhook_message_api_property);
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
        return Stream.of(
                Arguments.of(String.format("{\"webhook\":\"%s\",\"channelName\":\"liorchannel\"}", webhook_message_api_property),webhook_message_api_property,"/?id=","/?status="),
                Arguments.of(String.format("{\"webhook\":\"%s\",\"channelName\":\"anotherone\"}",webhook_message_api_2_property),webhook_message_api_2_property,"/?id=","/?status=")
        );
    }
}
