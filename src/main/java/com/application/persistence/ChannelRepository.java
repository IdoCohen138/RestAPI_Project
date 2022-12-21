package com.application.persistence;

import com.application.persistence.exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.persistence.exceptions.ChannelNotExitsInDataBaseException;
import com.application.service.EnumStatus;
import com.application.service.SlackChannel;
import com.application.service.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChannelRepository implements Repository {

    private final List<SlackChannel> channels = new ArrayList<>();

    @Override
    public void createChannel(SlackChannel newChannel) throws ChannelAlreadyExitsInDataBaseException {
        if (!channelExistsByWebhook(newChannel)){
            channels.add(newChannel);
            return;
        }
        throw new ChannelAlreadyExitsInDataBaseException("This channel already exits in database");
    }


    @Override
    public SlackChannel deleteChannel(UUID id) throws ChannelNotExitsInDataBaseException {
        SlackChannel deletedChannel = deleteChannelFromData(id);
        if (deletedChannel == null)
            throw new ChannelNotExitsInDataBaseException("This channel not exits in the database");
        return deletedChannel;
    }

    @Override
    public SlackChannel getChannel(UUID uuid) throws ChannelNotExitsInDataBaseException {
        if (!channelExistsById(uuid)){
            throw new ChannelNotExitsInDataBaseException("This channel not exits in the database");
        }
        return getChannelById(uuid);
    }

    @Override
    public List<SlackChannel> getChannels(EnumStatus filter) {
        return sortArrayByFiltering(filter);
    }

    @Override
    public List<SlackChannel> getAllChannels() {
        return channels;
    }


    private SlackChannel getChannelById(UUID id) {
        for (SlackChannel modifyChannel : channels) {
            if (id.equals(modifyChannel.getId()))
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

    private SlackChannel deleteChannelFromData(UUID id) {
        if(channelExistsById(id)){
            SlackChannel deletedChannel = getChannelById(id);
            channels.remove(deletedChannel);
            return deletedChannel;
        }
        return null;
    }

    private List<SlackChannel> sortArrayByFiltering(EnumStatus filter) {
        List<SlackChannel> filterArray = new ArrayList<>();
        for (SlackChannel channel : channels) {
            if (channel.getStatus().equals(filter))
                filterArray.add(channel);
        }
        return filterArray;
    }

    private Boolean channelExistsByWebhook(SlackChannel channel){
        return getChannelByWebhook(channel.getWebhook()) != null;
    }

    private boolean channelExistsById(UUID id) {
        return getChannelById(id) != null;
    }
}
