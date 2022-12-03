package com.application;

import com.application.presentationLayer.DataAccess;
import com.application.presentationLayer.Exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.presentationLayer.Exceptions.ChannelNotExitsInDataBaseException;
import com.application.serviceLayer.*;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.slack.api.Slack;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.http.HttpClient;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class integrationTest {
    Slack slack;
    SlackChannelController slackChannelController;
    SlackChannel slackChannel;
    SlackIntegration slackIntegration;
    Repository DataAccess;
    ArrayList<SlackChannel> channels;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {

        slackChannelController = new SlackChannelController();
        slack= Mockito.mock(Slack.class);
        slackChannel=new SlackChannel();
        slackChannel.setId(UUID.randomUUID());
        slackChannel.setWebhook("https://hooks.slack.com/services/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");
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

    }    @Test
    void getAllChannelsTest() throws ChannelAlreadyExitsInDataBaseException {
        slackChannelController.createChannel(slackChannel);
        slackChannel.setStatus(EnumStatus.ENABLED);
        ArrayList<SlackChannel> slackChannelreturn=
                (ArrayList<SlackChannel>) assertDoesNotThrow(() -> slackChannelController.getChannels("ENABLED"));
        assertEquals(slackChannelreturn.get(0),slackChannel);

    }




}