package com.pack;

import com.pack.exceptions.ChannelAlreadyExitsInDataBaseException;
import com.pack.exceptions.ChannelNotExitsInDataBaseException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface Persistent {

    void saveChannel(SlackChannel slackChannel) throws ChannelAlreadyExitsInDataBaseException;

    SlackChannel updateChannel(UUID id, EnumStatus status) throws ChannelNotExitsInDataBaseException;

    SlackChannel deleteChannel(UUID id) throws ChannelNotExitsInDataBaseException;

    SlackChannel getChannelbyID(UUID uuid) throws ChannelNotExitsInDataBaseException;

    List<SlackChannel> getAllChannelsbyStatus(EnumStatus filter);

    List<SlackChannel> getAllChannels();
}
