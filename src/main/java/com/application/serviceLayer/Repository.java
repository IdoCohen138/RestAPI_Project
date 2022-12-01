package com.application.serviceLayer;

import com.application.presentationLayer.Exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.presentationLayer.Exceptions.ChannelNotExitsInDataBaseException;

import java.util.ArrayList;
import java.util.UUID;

public interface Repository {


    void createChannel(SlackChannel newChannel) throws ChannelAlreadyExitsInDataBaseException;
    SlackChannel updateChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException;
    SlackChannel deleteChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException;
    SlackChannel getSpecificChannel(UUID uuid) throws ChannelNotExitsInDataBaseException;
    ArrayList<?> getChannels(String filter);
    ArrayList<?> getAllChannels();
}
