package com.application.service;

import com.application.persistence.ChannelRepository;
import com.application.persistence.exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.persistence.exceptions.ChannelNotExitsInDataBaseException;
import com.application.service.exceptions.SlackMessageNotSentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class SlackControllerTest {

    @InjectMocks
    @Resource
    SlackChannelController slackChannelController;


    @Mock
    ChannelRepository channelRepository;
    @Mock
    SlackIntegration slackIntegration;

    SlackChannel slackChannel;
    List<SlackChannel> slackChannels;


    @BeforeEach
    public void setUp() {
        slackChannel = new SlackChannel();
        MockitoAnnotations.openMocks(this);

        slackChannels = new ArrayList<>();
        slackChannels.add(slackChannel);

    }
    public void createChannelTestSuccess() throws ChannelAlreadyExitsInDataBaseException, SlackMessageNotSentException {
        slackChannelController.createChannel(slackChannel);
        Mockito.verify(channelRepository).createChannel(slackChannel);
        Mockito.verify(slackIntegration).sendMessage(slackChannel, "New channel has been created");

    }

    @Test
    public void updateChannelTestSuccess() throws SlackMessageNotSentException, ChannelNotExitsInDataBaseException {
        Mockito.when(channelRepository.updateChannel(slackChannel)).thenReturn(slackChannel);
        slackChannelController.updateChannel(slackChannel);
        Mockito.verify(channelRepository).updateChannel(slackChannel);
        Mockito.verify(slackIntegration).sendMessage(slackChannel, "Channel's status has been updated");
    }

    @Test
    public void updateChannelTestFail_null_slack_channel() throws SlackMessageNotSentException, ChannelNotExitsInDataBaseException {
        Mockito.when(channelRepository.updateChannel(slackChannel)).thenReturn(null);
        slackChannelController.updateChannel(slackChannel);
        Mockito.verify(slackIntegration, Mockito.never()).sendMessage(slackChannel, "Channel's status has been updated");
    }

    @Test
    public void deleteChannelTestSuccess() throws SlackMessageNotSentException, ChannelNotExitsInDataBaseException {
        Mockito.when(channelRepository.deleteChannel(slackChannel)).thenReturn(slackChannel);
        slackChannelController.deleteChannel(slackChannel);
        Mockito.verify(channelRepository).deleteChannel(slackChannel);
        Mockito.verify(slackIntegration).sendMessage(slackChannel, "Channel has been deleted");

    }

    @Test
    public void deleteChannelTestFail_null_slack_channel() throws SlackMessageNotSentException, ChannelNotExitsInDataBaseException {
        Mockito.when(channelRepository.deleteChannel(slackChannel)).thenReturn(null);
        slackChannelController.deleteChannel(slackChannel);
        Mockito.verify(slackIntegration, Mockito.never()).sendMessage(slackChannel, "This channel not exits in the database");

    }

    @Test
    public void sendPeriodicMessagesTestSuccess() throws SlackMessageNotSentException {
        Mockito.when(channelRepository.getChannels(EnumStatus.ENABLED)).thenReturn(slackChannels);

        slackChannelController.sendPeriodicMessages();
        Mockito.verify(slackIntegration).sendMessage(slackChannel, "You have no vulnerabilities");

    }

    @Test
    public void sendPeriodicMessagesTestFail() throws SlackMessageNotSentException {
        Mockito.when(channelRepository.getChannels(EnumStatus.ENABLED)).thenReturn(slackChannels);
        SlackMessageNotSentException exception = new SlackMessageNotSentException("Message didn't send to slack");
        Mockito.when(slackIntegration.sendMessage(slackChannel, "You have no vulnerabilities")).thenThrow(exception);
        slackChannelController.sendPeriodicMessages();
        Mockito.verify(slackIntegration, Mockito.never()).sendMessage(slackChannel, "This channel not exits in the database");

    }


}
