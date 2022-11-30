package com.application.presentationLayer;

import com.application.presentationLayer.Exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.presentationLayer.Exceptions.ChannelNotExitsInDataBaseException;
import com.application.serviceLayer.SlackChannel;
import com.application.serviceLayer.Repository;
import com.application.serviceLayer.*;

import java.util.ArrayList;

public class DataAccess implements Repository {

    private final ArrayList<SlackChannel> channels = new ArrayList<>();

    @Override
    public void createChannel(SlackChannel newChannel) throws ChannelAlreadyExitsInDataBaseException{
        SlackChannel checkIfChannelExist = getChannel(newChannel);
        if (checkIfChannelExist==null) {
            channels.add(newChannel);
            return;
        }
        throw new ChannelAlreadyExitsInDataBaseException("This channel already exits in database");
    }

    @Override
    public SlackChannel updateChannel( SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException {
        SlackChannel channel = getChannel(slackChannel);
        if (channel== null){
            throw new ChannelNotExitsInDataBaseException("This channel not exits in the database");
        }
        return channel;
    }

    @Override
    public void deleteChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException {
        if (deleteChannelFromData(slackChannel).equals(EnumOperation.FAILURE))
            throw new ChannelNotExitsInDataBaseException("This channel not exits in the database");
    }

    @Override
    public SlackChannel getSpecificChannel(String webhook) throws ChannelNotExitsInDataBaseException {
        SlackChannel channel = getChannelByWebhook(webhook);
        if (channel==null){
            throw new ChannelNotExitsInDataBaseException("This channel not exits in the database");
        }
        return channel;
    }

    @Override
    public ArrayList<?> getChannels(String filter) {
        return sortArrayByFiltering(filter);
    }


    public SlackChannel getChannel(SlackChannel channel) {
        for (SlackChannel modifyChannel : channels) {
            if (channel.equals(modifyChannel))
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

    public EnumOperation deleteChannelFromData(SlackChannel slackChannel){
        SlackChannel toRemove = getChannel(slackChannel);
        if (toRemove != null) {
            channels.remove(toRemove);
            return EnumOperation.SUCCESS;
        }
        return EnumOperation.FAILURE;
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
