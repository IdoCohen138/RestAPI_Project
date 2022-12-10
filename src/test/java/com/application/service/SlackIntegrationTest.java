package com.application.service;

import com.application.persistence.exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.service.EnumStatus;
import com.application.service.exceptions.SlackMessageNotSentException;
import com.application.service.SlackChannel;
import com.application.service.SlackIntegration;
import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import java.io.PrintStream;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SlackIntegrationTest {
    Slack slack;
    SlackIntegration slackIntegration;
    SlackChannel slackChannel;
    HttpHeaders headers;

    @BeforeEach
    public void setup() throws IOException {

        slack = Mockito.mock(Slack.class);
        //set new channel
        slackChannel = new SlackChannel();
        slackChannel.setId(UUID.randomUUID());
        slackChannel.setWebhook("SOME_WEBHOOK1");
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

//    @Test
//    void test_controller_slack_message_fails_to_send_message() throws SlackMessageNotSentException, ChannelAlreadyExitsInDataBaseException {
//        SlackIntegration slackIntegration = Mockito.mock(SlackIntegration.class);
//        SlackMessageNotSentException exception = new SlackMessageNotSentException("Message didn't send to slack");
//        when(slackIntegration.sendMessage(any(),any())).thenThrow(exception);
//
//        ReflectionTestUtils.setField(ChannelRepository,"slackIntegration",slackIntegration);
//
//        //to check if the thrown is printed
//        System.setOut(new PrintStream(byteArrayOutputStream));
//
//        slackChannelController.createChannel(slackChannel);
//        assertEquals("Message didn't send to slack", byteArrayOutputStream.toString().trim());
//
//        System.setOut(standardOut);
//    }

    private static Stream<Arguments> webhooks() throws IOException {
        return Stream.of(
                Arguments.of("{\"webhook\":\"SOME_WEBHOOK1\",\"channelName\":\"liorchannel\"}", "SOME_WEBHOOK1","/?id=","/?status="),
                Arguments.of("{\"webhook\":\"SOME_WEBHOOK1\",\"channelName\":\"anotherone\"}","SOME_WEBHOOK2","/?id=","/?status=")
        );
    }
}