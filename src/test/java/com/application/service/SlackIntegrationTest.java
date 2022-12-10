package com.application.service;

import com.application.service.exceptions.SlackMessageNotSentException;
import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.IOException;

public class SlackIntegrationTest {

    SlackIntegration slackIntegration;

    @Mock
    Slack slack;

    SlackChannel slackChannel;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        set_slackChannel_properties();
        slackIntegration = new SlackIntegration();
        slackIntegration.setSlack(slack);
    }

    @Test
    public void sendMessageTest() throws SlackMessageNotSentException, IOException {

        slackIntegration.sendMessage(slackChannel, "SOME_MESSAGE");

        Payload payload = Payload.builder().text("SOME_MESSAGE").build();

        Mockito.verify(slack).send(slackChannel.getWebhook(), payload);

    }

    private void set_slackChannel_properties() {
        slackChannel = new SlackChannel();
        slackChannel.setWebhook("SOME_WEBHOOK");
    }

}
