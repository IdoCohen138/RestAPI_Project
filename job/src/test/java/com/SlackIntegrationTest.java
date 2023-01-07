package com;

import exceptions.SlackMessageNotSentException;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class SlackIntegrationTest {

    @InjectMocks
    @Resource
    SlackIntegration slackIntegration;

    @Mock
    MessageRepository messageRepository;
    @Mock
    SlackRepository slackRepository;

    @Mock
    MessageSender messageSender;

    SlackChannel slackChannel;
    List<SlackChannel> slackChannels;


    @BeforeEach
    public void setup() throws IOException {
        MockitoAnnotations.openMocks(this);
        slackChannel=createSlackChannel();
        slackChannels = new ArrayList<>();
        slackChannels.add(slackChannel);

    }



    @Test
    public void sendPeriodicMessagesTestSuccess() throws SlackMessageNotSentException {
        Mockito.when(slackRepository.findAll((Specification<SlackChannel>) any())).thenReturn(slackChannels);
//        Mockito.when(messageSender.sendMessage(slackChannel,"You have no vulnerabilities")).th;

        slackIntegration.sendPeriodicMessages();
        Mockito.verify(messageRepository).save(any() );

    }

    @Test
    public void sendPeriodicMessagesTestFail() throws  SlackMessageNotSentException {
        Mockito.when(slackRepository.findAll((Specification<SlackChannel>) any())).thenReturn(slackChannels);
        SlackMessageNotSentException exception = new SlackMessageNotSentException("Message didn't send to slack");
        Mockito.when(messageSender.sendMessage(slackChannel,"You have no vulnerabilities")).thenThrow(exception);
        slackIntegration.sendPeriodicMessages();
        Mockito.verify(messageRepository,Mockito.never()).save(any() );

    }


    private SlackChannel createSlackChannel() throws IOException {
        SlackChannel slackChannel;
        slackChannel = new SlackChannel();
        String webhook = readFromProperties();
        slackChannel.setWebhook(webhook);
        slackChannel.setChannelName("test");
        slackChannel.setStatus(EnumStatus.ENABLED);
        slackChannel.setLogMessages(new HashSet<>());
        return slackChannel;

    }

    private String readFromProperties() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        return properties.getProperty("webhook_message_api");
    }

}
