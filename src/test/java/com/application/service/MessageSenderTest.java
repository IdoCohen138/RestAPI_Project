package com.application.service;

import com.application.job.SlackIntegration;
import com.application.service.exceptions.SlackMessageNotSentException;
import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)

public class MessageSenderTest {

    @InjectMocks
    @Resource
    MessageSender messageSender;

    @Mock
    Slack slack;
    SlackChannel slackChannel;
    List<SlackChannel> slackChannels;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        set_slackChannel_properties();
        slackChannels = new ArrayList<>();
        slackChannels.add(slackChannel);

    }
    private void set_slackChannel_properties() {
        slackChannel = new SlackChannel();
        slackChannel.setWebhook("SOME_WEBHOOK");
    }


    @Test
    public void sendMessageTestSuccess() throws SlackMessageNotSentException, IOException {

        messageSender.sendMessage(slackChannel, "SOME_MESSAGE");
        Payload payload = Payload.builder().text("SOME_MESSAGE").build();

        Mockito.verify(slack).send(slackChannel.getWebhook(), payload);

    }



}
