package com.application.service;

import com.application.persistence.exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.persistence.exceptions.ChannelNotExitsInDataBaseException;
import com.application.service.exceptions.SlackMessageNotSentException;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class SlackControllerTest {

    @InjectMocks
    @Resource
    SlackChannelController slackChannelController;

    @Mock
    MessageRepository messageRepository;

    @Mock
    SlackRepository channelRepository;
    @Mock
    SlackIntegration slackIntegration;

    SlackChannel slackChannel;
    List<SlackChannel> slackChannels;

    @Before
    @BeforeEach
    public void setUp() {
        slackChannel = new SlackChannel();
        slackChannel.setStatus(EnumStatus.ENABLED);
        MockitoAnnotations.openMocks(this);
        slackChannels = new ArrayList<>();
        slackChannels.add(slackChannel);
    }

    @Test
    public void createChannelTestSuccess() throws ChannelAlreadyExitsInDataBaseException, SlackMessageNotSentException {
        Mockito.when(channelRepository.save(slackChannel)).thenReturn(slackChannel);
        slackChannelController.createChannel(slackChannel);
        Mockito.verify(channelRepository, times(1)).save(slackChannel);
        Mockito.verify(slackIntegration).sendMessage(slackChannel, "New channel has been created");
    }

    @Test
    void CreateChannelTest_channelAlreadyExits() {
        Mockito.when(channelRepository.save(slackChannel)).thenThrow(DataIntegrityViolationException.class);
        Assertions.assertThrows(ChannelAlreadyExitsInDataBaseException.class, () -> {
            slackChannelController.createChannel(slackChannel);
        });
    }


    @Test
    public void updateChannelTestEnabledSuccess() throws SlackMessageNotSentException, ChannelNotExitsInDataBaseException {
        Mockito.when(channelRepository.findById(slackChannel.getId())).thenReturn(Optional.of(slackChannel));
        slackChannelController.updateChannel(slackChannel.getId(), EnumStatus.ENABLED);
        Mockito.verify(channelRepository).updateChannel(slackChannel.getId(), EnumStatus.ENABLED);
        Mockito.verify(slackIntegration).sendMessage(slackChannel, "Channel's status has been updated");
    }

    @Test
    public void updateChannelTestDisabledSuccess() throws SlackMessageNotSentException, ChannelNotExitsInDataBaseException {
        slackChannel.setStatus(EnumStatus.DISABLED);
        Mockito.when(channelRepository.findById(slackChannel.getId())).thenReturn(Optional.of(slackChannel));
        slackChannelController.updateChannel(slackChannel.getId(), EnumStatus.DISABLED);
        Mockito.verify(channelRepository).updateChannel(slackChannel.getId(), EnumStatus.DISABLED);
        Mockito.verify(slackIntegration, Mockito.never()).sendMessage(slackChannel, "Channel's status has been updated");
    }


    @Test
    public void updateChannelTestFail_null_slack_channel() {
        Mockito.when(channelRepository.findById(slackChannel.getId())).thenThrow(new NoSuchElementException());
        Assertions.assertThrows(ChannelNotExitsInDataBaseException.class, () -> {
            slackChannelController.updateChannel(slackChannel.getId(), EnumStatus.ENABLED);
        });
    }

    @Test
    public void deleteChannelTestEnableSuccess() throws SlackMessageNotSentException, ChannelNotExitsInDataBaseException {
        Mockito.when(channelRepository.findById(slackChannel.getId())).thenReturn(Optional.of(slackChannel));
        slackChannelController.deleteChannel(slackChannel.getId());
        Mockito.verify(channelRepository).delete(slackChannel);
        Mockito.verify(slackIntegration).sendMessage(slackChannel, "Channel has been deleted");

    }

    @Test
    public void deleteChannelTestDisableSuccess() throws SlackMessageNotSentException, ChannelNotExitsInDataBaseException {
        slackChannel.setStatus(EnumStatus.DISABLED);
        Mockito.when(channelRepository.findById(slackChannel.getId())).thenReturn(Optional.of(slackChannel));
        slackChannelController.deleteChannel(slackChannel.getId());
        Mockito.verify(channelRepository).delete(slackChannel);
        Mockito.verify(slackIntegration, Mockito.never()).sendMessage(slackChannel, "Channel has been deleted");
    }

    @Test
    public void deleteChannelTestFail_null_slack_channel() {
        Mockito.when(channelRepository.findById(slackChannel.getId())).thenThrow(new NoSuchElementException());
        Assertions.assertThrows(ChannelNotExitsInDataBaseException.class, () -> {
            slackChannelController.deleteChannel(slackChannel.getId());
        });
    }


    @Test
    public void sendPeriodicMessagesTestSuccess() throws SlackMessageNotSentException {
        Mockito.when(channelRepository.findAll((Specification<SlackChannel>) any())).thenReturn(slackChannels);
        slackChannelController.sendPeriodicMessages();
        Mockito.verify(slackIntegration).sendMessage(slackChannel, "You have no vulnerabilities");

    }

    @Test
    public void sendPeriodicMessagesTestFail() throws SlackMessageNotSentException {
        Mockito.when(channelRepository.findAll((Specification<SlackChannel>) any())).thenReturn(slackChannels);
        SlackMessageNotSentException exception = new SlackMessageNotSentException("Message didn't send to slack");
        Mockito.when(slackIntegration.sendMessage(slackChannel, "You have no vulnerabilities")).thenThrow(exception);
        slackChannelController.sendPeriodicMessages();
        Mockito.verify(slackIntegration, Mockito.never()).sendMessage(slackChannel, "This channel not exits in the database");

    }


}
