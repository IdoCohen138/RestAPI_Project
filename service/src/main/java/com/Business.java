package com;

import exceptions.ChannelAlreadyExitsInDataBaseException;
import exceptions.ChannelNotExitsInDataBaseException;


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
