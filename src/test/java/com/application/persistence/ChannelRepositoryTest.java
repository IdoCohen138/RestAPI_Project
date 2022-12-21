package com.application.persistence;

import com.application.persistence.exceptions.ChannelNotExitsInDataBaseException;
import com.application.service.EnumStatus;
import com.application.service.Repository;
import com.application.service.SlackChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ChannelRepositoryTest {
    SlackChannel slackChannel;
    Repository ChannelRepository;
    List<SlackChannel> channels;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException, IOException {
        createSlackChannel();
        ChannelRepository = new ChannelRepository();

        Field channels_ = ChannelRepository.getClass().getDeclaredField("channels");
        channels_.setAccessible(true);
        channels = (ArrayList<SlackChannel>) channels_.get(ChannelRepository);
    }


    @Test
    void createChannelTestSuccess() {
        assertEquals(0, channels.size());
        assertDoesNotThrow(() -> ChannelRepository.createChannel(slackChannel));
        assertEquals(1, channels.size());
    }

    @Test
    void deleteChannelTestSuccess() {
        assertDoesNotThrow(() -> ChannelRepository.createChannel(slackChannel));
        assertDoesNotThrow(() -> ChannelRepository.deleteChannel(slackChannel.getId()));
        assertEquals(0, channels.size());
    }

    @Test
    void deleteChannel_no_slack_channel_to_delete_TestFail() {
        assertThrows(ChannelNotExitsInDataBaseException.class, () -> {
            ChannelRepository.deleteChannel(slackChannel.getId());
        });
    }


    @Test
    void getSpecificChannelTestSuccess() {
        assertDoesNotThrow(() -> ChannelRepository.createChannel(slackChannel));
        SlackChannel slackChannelreturn =
                assertDoesNotThrow(() -> ChannelRepository.getChannel(slackChannel.getId()));
        assertEquals(slackChannelreturn, slackChannel);
    }

    @Test
    void getSpecificChannel_no_slack_channel_to_get_TestFail() {
        assertThrows(ChannelNotExitsInDataBaseException.class, () -> {
            ChannelRepository.getChannel(slackChannel.getId());
        });
    }


    @Test
    void getChannels_byFilter_enable_TestSuccess() {
        assertDoesNotThrow(() -> ChannelRepository.createChannel(slackChannel));
        List<SlackChannel> slackChannelreturn_enable =
                assertDoesNotThrow(() -> ChannelRepository.getChannels(EnumStatus.ENABLED));
        assertEquals(slackChannelreturn_enable, channels);
    }

    @Test
    void getChannels_byFilter_enable_return_nothing_TestSuccess() {
        assertDoesNotThrow(() -> ChannelRepository.createChannel(slackChannel));
        List<SlackChannel> slackChannelreturn_disable =
                assertDoesNotThrow(() -> ChannelRepository.getChannels(EnumStatus.DISABLED));
        assertEquals(slackChannelreturn_disable.size(), 0);
    }

    @Test
    void getChannels_byFilter_disable_TestSuccess() {
        slackChannel.setStatus(EnumStatus.DISABLED);
        List<SlackChannel> slackChannelreturn_disable =
                assertDoesNotThrow(() -> ChannelRepository.getChannels(EnumStatus.DISABLED));
        assertEquals(slackChannelreturn_disable, channels);
    }

    @Test
    void getChannels_byFilter_disable_return_nothing_TestSuccess() {
        slackChannel.setStatus(EnumStatus.DISABLED);
        assertDoesNotThrow(() -> ChannelRepository.createChannel(slackChannel));
        List<SlackChannel> slackChannelreturn_enable =
                assertDoesNotThrow(() -> ChannelRepository.getChannels(EnumStatus.ENABLED));
        assertEquals(slackChannelreturn_enable.size(), 0);
    }


    @Test
    void getAllChannelsTestSuccess() {
        assertDoesNotThrow(() -> ChannelRepository.createChannel(slackChannel));
        List<SlackChannel> slackChannelreturn = ChannelRepository.getAllChannels();
        assertEquals(slackChannelreturn, channels);
    }

    @Test
    void getAllChannels_return_nothing_TestSuccess() {
        List<SlackChannel> slackChannelreturn = ChannelRepository.getAllChannels();
        assertEquals(slackChannelreturn, channels);
    }


    private void createSlackChannel() throws IOException {
        slackChannel = new SlackChannel();
        slackChannel.setId(UUID.randomUUID());
        String webhook = readFromProperties();
        slackChannel.setWebhook(webhook);
        slackChannel.setStatus(EnumStatus.ENABLED);

    }


    private String readFromProperties() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        return properties.getProperty("webhook_message_api");
    }
}