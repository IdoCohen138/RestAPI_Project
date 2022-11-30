package com.application.serviceLayer;

import com.application.presentationLayer.Exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.presentationLayer.Exceptions.ChannelNotExitsInDataBaseException;
import com.application.presentationLayer.DataAccess;

import java.util.ArrayList;

public class SlackChannelController implements ChannelRepository {
    Repository repository = new DataAccess();

    @Override
    public void createChannel(SlackChannel slackChannel) throws ChannelAlreadyExitsInDataBaseException {
        repository.createChannel(slackChannel);
    }

    @Override
    public void updateChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException {
        SlackChannel modifyChannel = repository.updateChannel(slackChannel);
        if (modifyChannel.getStatus().equals(EnumStatus.DISABLED)){
            modifyChannel.setStatus(EnumStatus.ENABLED);
        }
        else {
            modifyChannel.setStatus(EnumStatus.DISABLED);
        }
    }

    @Override
    public void deleteChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException {
        repository.deleteChannel(slackChannel);
    }

    @Override
    public SlackChannel getSpecificChannel(String webhook) throws ChannelNotExitsInDataBaseException {
        return repository.getSpecificChannel(webhook);
    }

    @Override
    public ArrayList<?> getChannels(String filter) {
        return repository.getChannels(filter);
    }

}
