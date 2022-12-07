package com.application.persistence;

import com.application.persistence.exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.persistence.exceptions.ChannelNotExitsInDataBaseException;
import com.application.service.SlackChannel;
import com.application.service.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class DataAccess implements Repository {

    private final ArrayList<SlackChannel> channels = new ArrayList<>();

    @Override
    public void createChannel(SlackChannel newChannel) throws ChannelAlreadyExitsInDataBaseException {
        SlackChannel checkIfChannelExist = getChannelByWebhook(newChannel.getWebhook());
        if (checkIfChannelExist == null) {
            channels.add(newChannel);
            return;
        }
        throw new ChannelAlreadyExitsInDataBaseException("This channel already exits in database");
    }

    @Override
    public SlackChannel updateChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException {
        SlackChannel channel = getChannel(slackChannel);
        if (channel == null) {
            throw new ChannelNotExitsInDataBaseException("This channel not exits in the database");
        }
        return channel;
    }

    @Override
    public SlackChannel deleteChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException {
        SlackChannel deletedChannel = deleteChannelFromData(slackChannel);
        if (deletedChannel == null)
            throw new ChannelNotExitsInDataBaseException("This channel not exits in the database");
        return deletedChannel;
    }

    @Override
    public SlackChannel getSpecificChannel(UUID uuid) throws ChannelNotExitsInDataBaseException {
        SlackChannel toSearch = new SlackChannel();
        toSearch.setId(uuid);
        SlackChannel channel = getChannel(toSearch);
        if (channel == null) {
            throw new ChannelNotExitsInDataBaseException("This channel not exits in the database");
        }
        return channel;
    }

    @Override
    public ArrayList<?> getChannels(String filter) {
        return sortArrayByFiltering(filter);
    }

    @Override
    public ArrayList<?> getAllChannels() {
        return channels;
    }

    public SlackChannel getChannel(SlackChannel channel) {
        for (SlackChannel modifyChannel : channels) {
            if (channel.equals(modifyChannel)) //check by webhook
                return modifyChannel;
        }
        return null;
    }

    private SlackChannel getChannelByWebhook(String webhook) {
        for (SlackChannel modifyChannel : channels) {
            if (webhook.equals(modifyChannel.getWebhook()))
                return modifyChannel;
        }
        return null;
    }

    public SlackChannel deleteChannelFromData(SlackChannel slackChannel) {
        SlackChannel toRemove = getChannel(slackChannel);
        if (toRemove != null) {
            channels.remove(toRemove);
            return toRemove;
        }
        return null;
    }

    public ArrayList<SlackChannel> sortArrayByFiltering(String filter) {
        if (filter.equals("None"))
            return channels;
        ArrayList<SlackChannel> filterArray = new ArrayList<>();
        for (SlackChannel channel : channels) {
            if (channel.getStatus().toString().equals(filter))
                filterArray.add(channel);
        }
        return filterArray;
    }
}
