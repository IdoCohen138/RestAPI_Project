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
import org.springframework.data.jpa.domain.Specification;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class SlackIntegrationTest {

    @InjectMocks
    @Resource
    SlackIntegration slackIntegration;

    @Mock
    Slack slack;
    @Mock
    MessageRepository messageRepository;
    @Mock
    SlackRepository slackRepository;
    SlackChannel slackChannel;
    List<SlackChannel> slackChannels;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        set_slackChannel_properties();
        slackChannels = new ArrayList<>();
        slackChannels.add(slackChannel);

    }


    @Test
    public void sendMessageTestSuccess() throws SlackMessageNotSentException, IOException {

        slackIntegration.sendMessage(slackChannel, "SOME_MESSAGE");
        Payload payload = Payload.builder().text("SOME_MESSAGE").build();

        Mockito.verify(slack).send(slackChannel.getWebhook(), payload);

    }

    private void set_slackChannel_properties() {
        slackChannel = new SlackChannel();
        slackChannel.setWebhook("SOME_WEBHOOK");
    }


    @Test
    public void sendPeriodicMessagesTestSuccess()  {
        Mockito.when(slackRepository.findAll((Specification<SlackChannel>) any())).thenReturn(slackChannels);
        slackIntegration.sendPeriodicMessages();
        Mockito.verify(messageRepository).save(any() );

    }

    @Test
    public void sendPeriodicMessagesTestFail() throws  IOException {
        Mockito.when(slackRepository.findAll((Specification<SlackChannel>) any())).thenReturn(slackChannels);
        Payload payload = Payload.builder().text("You have no vulnerabilities").build();
        IOException exception = new IOException();
        Mockito.when(slack.send(slackChannel.getWebhook(),payload)).thenThrow(exception);
        slackIntegration.sendPeriodicMessages();
        Mockito.verify(messageRepository,Mockito.never()).save(any() );

    }

}
