package com.application.persistence;

import com.application.persistence.exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.persistence.exceptions.ChannelNotExitsInDataBaseException;
import com.application.service.*;
import com.application.service.exceptions.SlackMessageNotSentException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ChannelRepositoryTest {
    SlackChannel slackChannel;
    PersistenceInterface ChannelRepository;
    List<SlackChannel> channels;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException , IOException{
        createSlackChannel();
        ChannelRepository =new ChannelRepository();

        Field channels_ = ChannelRepository.getClass().getDeclaredField("channels");
        channels_.setAccessible(true);
        channels = (ArrayList<SlackChannel>) channels_.get(ChannelRepository);

    }


    @Test
    void createChannelTestSuccess() {
        assertEquals(0,channels.size());
        assertDoesNotThrow(() -> ChannelRepository.createChannel(slackChannel));
        assertEquals(1,channels.size());

    }
    @Test
    void updateChannelTestSuccess() {
        assertDoesNotThrow(() -> ChannelRepository.createChannel(slackChannel));
        assertDoesNotThrow(() -> ChannelRepository.updateChannel(slackChannel));
        assertEquals(slackChannel.getStatus(),channels.get(0).getStatus());

    }

    @Test
    void updateChannelTestfail() {
        assertThrows(ChannelNotExitsInDataBaseException.class, () -> { ChannelRepository.updateChannel(slackChannel);});
    }


    @Test
    void deleteChannelTestSuccess() {

        assertDoesNotThrow(() -> ChannelRepository.createChannel(slackChannel));
        assertDoesNotThrow(() -> ChannelRepository.deleteChannel(slackChannel));
        assertEquals(0,channels.size());
    }

    @Test
    void deleteChannelTestFail() {
        assertThrows(ChannelNotExitsInDataBaseException.class, () -> { ChannelRepository.deleteChannel(slackChannel);});
    }


    @Test
    void getSpecificChannelTestSuccess()   {
        assertDoesNotThrow(() -> ChannelRepository.createChannel(slackChannel));
        SlackChannel slackChannelreturn=
        assertDoesNotThrow(() -> ChannelRepository.getChannel(slackChannel.getId()));
        assertEquals(slackChannelreturn,slackChannel);

    }
    @Test
    void getSpecificChannelTestFail() {
        assertThrows(ChannelNotExitsInDataBaseException.class, () -> { ChannelRepository.getChannel(slackChannel.getId());});
    }


    @Test
    void getChannels_byfilter_enable_TestSuccess()  {
        assertDoesNotThrow(() -> ChannelRepository.createChannel(slackChannel));
        List<SlackChannel> slackChannelreturn_enable=
                assertDoesNotThrow(() -> ChannelRepository.getChannels(EnumStatus.ENABLED));
        assertEquals(slackChannelreturn_enable,channels);

    }
    @Test
    void getChannels_byfilter_enable_return_nothing_TestSuccess()  {
        assertDoesNotThrow(() -> ChannelRepository.createChannel(slackChannel));
        List<SlackChannel> slackChannelreturn_disable=
                assertDoesNotThrow(() -> ChannelRepository.getChannels(EnumStatus.DISABLED));
        assertEquals(slackChannelreturn_disable.size(),0);

    }

    @Test
    void getChannels_byfilter_disable_TestSuccess()  {
        slackChannel.setStatus(EnumStatus.DISABLED);
        List<SlackChannel> slackChannelreturn_disable=
                assertDoesNotThrow(() -> ChannelRepository.getChannels(EnumStatus.DISABLED));
        assertEquals(slackChannelreturn_disable,channels);

    }
    @Test
    void getChannels_byfilter_disable_return_nothing_TestSuccess()  {
        slackChannel.setStatus(EnumStatus.DISABLED);
        assertDoesNotThrow(() -> ChannelRepository.createChannel(slackChannel));
        List<SlackChannel> slackChannelreturn_enable=
                assertDoesNotThrow(() -> ChannelRepository.getChannels(EnumStatus.ENABLED));
        assertEquals(slackChannelreturn_enable.size(),0);

    }


    @Test
    void getAllChannelsTestSuccess() {
        assertDoesNotThrow(() -> ChannelRepository.createChannel(slackChannel));
        List<SlackChannel> slackChannelreturn= ChannelRepository.getAllChannels();
        assertEquals(slackChannelreturn,channels);
    }

    @Test
    void getAllChannelsTest_return_nothing_Success() {
        List<SlackChannel> slackChannelreturn= ChannelRepository.getAllChannels();
        assertEquals(slackChannelreturn,channels);
    }



    private void createSlackChannel() throws IOException {
        slackChannel=new SlackChannel();
        String webhook = readFromProperties();
        slackChannel.setWebhook(webhook);
        slackChannel.setStatus(EnumStatus.ENABLED);

    }


    private String readFromProperties() throws IOException{
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        return properties.getProperty("webhook_message_api");
    }
}