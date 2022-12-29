package com.application.persistence;

import com.application.persistence.exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.persistence.exceptions.ChannelNotExitsInDataBaseException;
import com.application.service.EnumStatus;
import com.application.service.Repository;
import com.application.service.SlackChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ChannelRepositoryTest {
    SlackChannel slackChannel;
    Repository ChannelRepository;
    List<SlackChannel> slackChannels;

    public static Stream<Arguments> enumStatus() {

        return Stream.of(
                Arguments.of(EnumStatus.ENABLED, EnumStatus.DISABLED),
                Arguments.of(EnumStatus.DISABLED, EnumStatus.ENABLED)
        );
    }


    @BeforeEach
    public void setUp() throws IOException {
        createSlackChannel();
        ChannelRepository = new ChannelRepository();
        slackChannels = new ArrayList<>();

    }


    @Test
    void createChannelTestSuccess() throws ChannelNotExitsInDataBaseException {
        assertEquals(0, ChannelRepository.getAllChannels().size());
        assertDoesNotThrow(() -> ChannelRepository.createChannel(slackChannel));
        slackChannels.add(slackChannel);
        slackChannel.setCreated_at(new Timestamp(System.currentTimeMillis()));
        assertEquals(ChannelRepository.getAllChannels(), slackChannels);
        assertEquals((ChannelRepository.getChannel(slackChannel.getId()).getCreated_at()).toLocalDateTime().withNano(0), slackChannel.getCreated_at().toLocalDateTime().withNano(0));
        assertDoesNotThrow(() -> ChannelRepository.deleteChannel(slackChannel.getId()));
        slackChannels.remove(slackChannel);

    }


    @Test
    void CreateChannelAlreadyExistFail() {
        assertDoesNotThrow(() -> ChannelRepository.createChannel(slackChannel));
        assertThrows(ChannelAlreadyExitsInDataBaseException.class, () -> ChannelRepository.createChannel(slackChannel));
        assertDoesNotThrow(() -> ChannelRepository.deleteChannel(slackChannel.getId()));

    }

    @Test
    void deleteChannelTestSuccess() {
        assertDoesNotThrow(() -> ChannelRepository.createChannel(slackChannel));
        assertDoesNotThrow(() -> ChannelRepository.deleteChannel(slackChannel.getId()));
        assertEquals(0, ChannelRepository.getAllChannels().size());
    }

    @Test
    void deleteChannel_no_slack_channel_to_delete_TestFail() {
        assertEquals(0, ChannelRepository.getAllChannels().size());
        assertThrows(ChannelNotExitsInDataBaseException.class, () -> {
            ChannelRepository.deleteChannel(slackChannel.getId());
        });
    }


    @Test
    void getSpecificChannelTestSuccess() {
        assertDoesNotThrow(() -> ChannelRepository.createChannel(slackChannel));
        SlackChannel channelReturn =
                assertDoesNotThrow(() -> ChannelRepository.getChannel(slackChannel.getId()));
        assertEquals(channelReturn, slackChannel);
        assertDoesNotThrow(() -> ChannelRepository.deleteChannel(slackChannel.getId()));
        slackChannels.remove(slackChannel);

    }

    @Test
    void getSpecificChannel_no_slack_channel_to_get_TestFail() {
        assertEquals(ChannelRepository.getAllChannels().size(), 0);
        assertThrows(ChannelNotExitsInDataBaseException.class, () -> {
            ChannelRepository.getChannel(slackChannel.getId());
        });
    }


    @ParameterizedTest
    @MethodSource("enumStatus")
    void getChannels_byFilter_TestSuccess(EnumStatus status1, EnumStatus status2) {
        assertDoesNotThrow(() -> ChannelRepository.createChannel(slackChannel));
        assertDoesNotThrow(() -> ChannelRepository.updateChannel(slackChannel.getId(), status1));
        slackChannel.setStatus(status1);
        List<SlackChannel> slackChannelreturn =
                assertDoesNotThrow(() -> ChannelRepository.getChannels(status1));
        slackChannels.add(slackChannel);
        assertEquals(slackChannelreturn, slackChannels);
        assertDoesNotThrow(() -> ChannelRepository.deleteChannel(slackChannel.getId()));
        slackChannels.remove(slackChannel);


    }

    @ParameterizedTest
    @MethodSource("enumStatus")
    void getChannels_byFilter_enable_return_nothing_TestSuccess(EnumStatus status1, EnumStatus status2) {
        assertDoesNotThrow(() -> ChannelRepository.createChannel(slackChannel));
        assertDoesNotThrow(() -> ChannelRepository.updateChannel(slackChannel.getId(), status1));
        slackChannel.setStatus(status1);
        List<SlackChannel> slackChannelreturn =
                assertDoesNotThrow(() -> ChannelRepository.getChannels(status2));
        slackChannels.add(slackChannel);
        assertNotEquals(slackChannelreturn, slackChannels);
        assertEquals(slackChannelreturn.size(), 0);
        assertDoesNotThrow(() -> ChannelRepository.deleteChannel(slackChannel.getId()));
        slackChannels.remove(slackChannel);

    }


    @Test
    void getAllChannelsTestSuccess() {
        assertDoesNotThrow(() -> ChannelRepository.createChannel(slackChannel));
        slackChannels.add(slackChannel);
        List<SlackChannel> slackChannelreturn_enable =
                assertDoesNotThrow(() -> ChannelRepository.getAllChannels());
        assertEquals(slackChannelreturn_enable, slackChannels);
        assertDoesNotThrow(() -> ChannelRepository.deleteChannel(slackChannel.getId()));
        slackChannels.remove(slackChannel);


    }

    @Test
    void getAllChannels_return_nothing_TestSuccess() {
        List<SlackChannel> slackChannelreturn = ChannelRepository.getAllChannels();
        assertEquals(slackChannelreturn, slackChannels);
    }


    @ParameterizedTest
    @MethodSource("enumStatus")
    public void updateChannelStatusTestSuccess(EnumStatus status1, EnumStatus status2) throws ChannelNotExitsInDataBaseException {
        assertDoesNotThrow(() -> ChannelRepository.createChannel(slackChannel));
        assertDoesNotThrow(() -> ChannelRepository.updateChannel(slackChannel.getId(), status1));
        slackChannel.setStatus(status1);
        slackChannels.add(slackChannel);
//        Date d=new Date();
        slackChannel.setModified_at(new Timestamp(System.currentTimeMillis()));
        assertEquals(ChannelRepository.getChannel(slackChannel.getId()).getStatus(), slackChannel.getStatus());
        assertEquals((ChannelRepository.getChannel(slackChannel.getId()).getModified_at()).toLocalDateTime().withNano(0), slackChannel.getModified_at().toLocalDateTime().withNano(0));
        assertDoesNotThrow(() -> ChannelRepository.deleteChannel(slackChannel.getId()));
        slackChannels.remove(slackChannel);


    }


    private void createSlackChannel() throws IOException {
        slackChannel = new SlackChannel();
        slackChannel.setId(UUID.randomUUID());
        String webhook = readFromProperties();
        slackChannel.setWebhook(webhook);
        slackChannel.setChannelName("test");
        slackChannel.setStatus(EnumStatus.ENABLED);

    }


    private String readFromProperties() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        return properties.getProperty("webhook_message_api");
    }
}