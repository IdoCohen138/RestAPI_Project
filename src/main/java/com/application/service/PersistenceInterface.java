package com.application.service;

import com.application.persistence.exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.persistence.exceptions.ChannelNotExitsInDataBaseException;

import java.util.List;
import java.util.UUID;

public interface PersistenceInterface {


    void createChannel(SlackChannel newChannel) throws ChannelAlreadyExitsInDataBaseException;

    SlackChannel updateChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException;

    SlackChannel deleteChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException;

    SlackChannel getChannel(UUID uuid) throws ChannelNotExitsInDataBaseException;

    List<SlackChannel> getChannels(EnumStatus filter);

    List<SlackChannel> getAllChannels();
}