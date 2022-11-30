package com.application.serviceLayer;
import com.application.presentationLayer.Exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.presentationLayer.Exceptions.ChannelNotExitsInDataBaseException;

import java.util.ArrayList;

public interface ChannelRepository {

    void createChannel(SlackChannel slackChannel) throws ChannelAlreadyExitsInDataBaseException;
    void updateChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException;
    void deleteChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException;
    SlackChannel getSpecificChannel(String webhook) throws ChannelNotExitsInDataBaseException;
    ArrayList<?> getChannels(String filter);
}