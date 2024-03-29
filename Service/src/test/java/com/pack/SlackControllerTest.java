package com.pack;

import com.pack.exceptions.ChannelAlreadyExitsInDataBaseException;
import com.pack.exceptions.ChannelNotExitsInDataBaseException;
import com.pack.exceptions.SlackMessageNotSentException;
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

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class SlackControllerTest {

    @InjectMocks
    @Resource
    SlackChannelController slackChannelController;

    @Mock
    MessageSender messageSender;

    @Mock
    Persistent channelRepository;

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
        slackChannelController.createChannel(slackChannel);
        Mockito.verify(channelRepository).saveChannel(slackChannel);
        Mockito.verify(channelRepository, times(1)).saveChannel(slackChannel);
        Mockito.verify(messageSender).sendMessage(slackChannel, "New channel has been created");
    }

    @Test
    void CreateChannelTest_channelAlreadyExits_throws_already_exists_exception() throws ChannelAlreadyExitsInDataBaseException {
        doThrow(new ChannelAlreadyExitsInDataBaseException("This channel already exits in database")).when(channelRepository).saveChannel(slackChannel);
        Assertions.assertThrows(ChannelAlreadyExitsInDataBaseException.class, () -> {
            slackChannelController.createChannel(slackChannel);
        });
    }


    @Test
    public void updateChannelTestEnabledSuccess() throws SlackMessageNotSentException, ChannelNotExitsInDataBaseException {
        Mockito.when(channelRepository.updateChannel(slackChannel.getId(), EnumStatus.ENABLED)).thenReturn(slackChannel);
        slackChannelController.updateChannel(slackChannel.getId(), EnumStatus.ENABLED);
        Mockito.verify(channelRepository).updateChannel(slackChannel.getId(), EnumStatus.ENABLED);
        Mockito.verify(messageSender).sendMessage(slackChannel, "Channel's status has been updated");
    }

    @Test
    public void updateChannelTestDisabledSuccess() throws SlackMessageNotSentException, ChannelNotExitsInDataBaseException {
        slackChannel.setStatus(EnumStatus.DISABLED);
        Mockito.when(channelRepository.updateChannel(slackChannel.getId(), EnumStatus.DISABLED)).thenReturn(slackChannel);
        slackChannelController.updateChannel(slackChannel.getId(), EnumStatus.DISABLED);
        Mockito.verify(channelRepository).updateChannel(slackChannel.getId(), EnumStatus.DISABLED);
        Mockito.verify(messageSender, Mockito.never()).sendMessage(slackChannel, "Channel's status has been updated");
    }


    @Test
    public void updateChannelTestFail_null_slack_channel() throws ChannelNotExitsInDataBaseException {
        Mockito.when(channelRepository.updateChannel(slackChannel.getId(),EnumStatus.ENABLED)).thenThrow(new ChannelNotExitsInDataBaseException("This channel not exits in the database"));
        Assertions.assertThrows(ChannelNotExitsInDataBaseException.class, () -> {
            slackChannelController.updateChannel(slackChannel.getId(), EnumStatus.ENABLED);
        });
    }

    @Test
    public void deleteChannelTestEnableSuccess() throws SlackMessageNotSentException, ChannelNotExitsInDataBaseException {
        Mockito.when(channelRepository.deleteChannel(slackChannel.getId())).thenReturn(slackChannel);
        slackChannelController.deleteChannel(slackChannel.getId());
        Mockito.verify(channelRepository).deleteChannel(slackChannel.getId());
        Mockito.verify(messageSender).sendMessage(slackChannel, "Channel has been deleted");

    }

    @Test
    public void deleteChannelTestDisableSuccess() throws SlackMessageNotSentException, ChannelNotExitsInDataBaseException {
        slackChannel.setStatus(EnumStatus.DISABLED);
        Mockito.when(channelRepository.deleteChannel(slackChannel.getId())).thenReturn(slackChannel);
        slackChannelController.deleteChannel(slackChannel.getId());
        Mockito.verify(messageSender, Mockito.never()).sendMessage(slackChannel, "Channel has been deleted");
    }

    @Test
    public void deleteChannelTestFail_null_slack_channel() throws ChannelNotExitsInDataBaseException {
            Mockito.when(channelRepository.deleteChannel(slackChannel.getId())).thenThrow(new ChannelNotExitsInDataBaseException("This channel not exits in the database"));
        Assertions.assertThrows(ChannelNotExitsInDataBaseException.class, () -> {
            slackChannelController.deleteChannel(slackChannel.getId());
        });
    }





}
