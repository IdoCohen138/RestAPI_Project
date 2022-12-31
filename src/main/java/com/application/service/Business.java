package com.application.service;

import com.application.persistence.exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.persistence.exceptions.ChannelNotExitsInDataBaseException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface Business {

    void createChannel(SlackChannel slackChannel) throws ChannelAlreadyExitsInDataBaseException;

    void updateChannel(UUID id, EnumStatus status) throws ChannelNotExitsInDataBaseException;

    void deleteChannel(UUID id) throws ChannelNotExitsInDataBaseException;

    SlackChannel getChannel(UUID uuid) throws ChannelNotExitsInDataBaseException;

    List<SlackChannel> getChannels(EnumStatus filter);

    List<SlackChannel> getAllChannels();

}
