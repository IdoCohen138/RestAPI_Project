package com.application;

import com.application.persistence.DataAccess;
import com.application.persistence.exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.service.*;
import com.application.service.exceptions.SlackMessageNotSentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

//@TestPropertySource("/config.properties")
public class IntegrationTest {
    SlackChannelController slackChannelController;
    SlackChannel slackChannel;
    Repository DataAccess;
    ArrayList<SlackChannel> channels;
    private Properties properties;

//    @Value("${webhook_message_api}")
//    static String webhook_message_api_property;

    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException, IOException {

        slackChannelController = new SlackChannelController();
        slackChannel=new SlackChannel();
        slackChannel.setId(UUID.randomUUID());

        //read from properties
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
        properties = new Properties();
        properties.load(inputStream);
        String webhook = properties.getProperty("webhook_message_api");

        slackChannel.setWebhook(webhook);
        slackChannel.setStatus(EnumStatus.ENABLED);

        DataAccess=new DataAccess();
        ReflectionTestUtils.setField(slackChannelController,"repository",DataAccess);

        Field channels_ = DataAccess.getClass().getDeclaredField("channels");
        channels_.setAccessible(true);
        channels = (ArrayList<SlackChannel>) channels_.get(DataAccess);

    }

    @Test
    void createChannelTest() {

        assertEquals(0,channels.size());
        assertDoesNotThrow(() -> slackChannelController.createChannel(slackChannel));
        assertEquals(1,channels.size());

    }
    @Test
    void updateChannelTest() throws ChannelAlreadyExitsInDataBaseException {

        slackChannelController.createChannel(slackChannel);
        assertEquals(slackChannel.getStatus(),channels.get(0).getStatus());
        slackChannel.setStatus(EnumStatus.ENABLED);
        assertDoesNotThrow(() -> slackChannelController.updateChannel(slackChannel));
        assertEquals(slackChannel.getStatus(),channels.get(0).getStatus());

    }
    @Test
    void deleteChannelTest() throws ChannelAlreadyExitsInDataBaseException {

        slackChannelController.createChannel(slackChannel);
        assertDoesNotThrow(() -> slackChannelController.deleteChannel(slackChannel));
        assertEquals(0,channels.size());

    }

    @Test
    void getSpecificChannelTest() throws ChannelAlreadyExitsInDataBaseException {
        slackChannelController.createChannel(slackChannel);
        SlackChannel slackChannelreturn=
        assertDoesNotThrow(() -> slackChannelController.getSpecificChannel(slackChannel.getId()));
        assertEquals(slackChannelreturn,slackChannel);

    }
    @Test
    void getChannelsTest() throws ChannelAlreadyExitsInDataBaseException {
        slackChannelController.createChannel(slackChannel);
        slackChannel.setStatus(EnumStatus.ENABLED);
        ArrayList<SlackChannel> slackChannelreturn=
                (ArrayList<SlackChannel>) assertDoesNotThrow(() -> slackChannelController.getChannels("ENABLED"));
        assertEquals(slackChannelreturn.get(0),slackChannel);

    }
    @Test
    void getAllChannelsTest() throws ChannelAlreadyExitsInDataBaseException {
        slackChannelController.createChannel(slackChannel);
        slackChannel.setStatus(EnumStatus.ENABLED);
        ArrayList<SlackChannel> slackChannelreturn=
                (ArrayList<SlackChannel>) assertDoesNotThrow(() -> slackChannelController.getChannels("ENABLED"));
        assertEquals(slackChannelreturn.get(0),slackChannel);
    }


    @Test
    void test_controller_slack_message_fails_to_send_message() throws SlackMessageNotSentException, ChannelAlreadyExitsInDataBaseException {
        SlackIntegration slackIntegration = Mockito.mock(SlackIntegration.class);
        SlackMessageNotSentException exception = new SlackMessageNotSentException("Message didn't send to slack");
        when(slackIntegration.sendMessage(any(),any())).thenThrow(exception);

        ReflectionTestUtils.setField(slackChannelController,"slackIntegration",slackIntegration);

        //to check if the thrown is printed
        System.setOut(new PrintStream(byteArrayOutputStream));

        slackChannelController.createChannel(slackChannel);
        assertEquals("Message didn't send to slack", byteArrayOutputStream.toString().trim());

        System.setOut(standardOut);
    }

}