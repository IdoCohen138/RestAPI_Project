package com.example.demo.serviceLayer;
import com.example.demo.presentationLayer.Exceptions.ChannelAlreadyExitsInDataBaseException;
import com.example.demo.presentationLayer.Exceptions.ChannelNotExitsInDataBaseException;

import java.util.ArrayList;

public interface channelRepository {

    void createChannel(SlackChannel slackChannel) throws ChannelAlreadyExitsInDataBaseException;
    void updateChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException;
    void deleteChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException;
    SlackChannel getSpecificChannel(String webhook) throws ChannelNotExitsInDataBaseException;
    ArrayList getChannels(String filter);
}
