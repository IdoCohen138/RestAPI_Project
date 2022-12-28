package com.application.service;

import com.application.persistence.exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.persistence.exceptions.ChannelNotExitsInDataBaseException;

import java.util.List;
import java.util.UUID;

public interface Repository {

    void createChannel(SlackChannel newChannel) throws ChannelAlreadyExitsInDataBaseException;

    SlackChannel deleteChannel(UUID id) throws ChannelNotExitsInDataBaseException;

    SlackChannel getChannel(UUID uuid) throws ChannelNotExitsInDataBaseException;

    List<SlackChannel> getChannels(EnumStatus filter);

    List<SlackChannel> getAllChannels();

    SlackChannel updateChannel(UUID uuid, EnumStatus status) throws ChannelNotExitsInDataBaseException;
}
