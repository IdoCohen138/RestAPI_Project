package com.application.service;

import com.application.persistence.Exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.persistence.Exceptions.ChannelNotExitsInDataBaseException;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {

    void createChannel(SlackChannel slackChannel) throws ChannelAlreadyExitsInDataBaseException;

    void updateChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException;

    void deleteChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException;

    SlackChannel getSpecificChannel(UUID uuid) throws ChannelNotExitsInDataBaseException;

    List<SlackChannel> getChannels(EnumStatus filter);

    List<SlackChannel> getAllChannels();

}
