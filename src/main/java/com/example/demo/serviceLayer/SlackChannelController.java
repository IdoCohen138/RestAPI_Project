package com.example.demo.serviceLayer;

import com.example.demo.presentationLayer.Exceptions.ChannelAlreadyExitsInDataBaseException;
import com.example.demo.presentationLayer.Exceptions.ChannelNotExitsInDataBaseException;
import com.example.demo.presentationLayer.dataBase;

import java.util.ArrayList;

public class SlackChannelController implements channelRepository{
    repository dataBaseInterface = new dataBase();

    @Override
    public void createChannel(SlackChannel slackChannel) throws ChannelAlreadyExitsInDataBaseException {
        dataBaseInterface.createChannel(slackChannel);
    }

    @Override
    public void updateChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException {
        SlackChannel modifyChannel = dataBaseInterface.updateChannel(slackChannel);
        modifyChannel.setStatus();
    }

    @Override
    public void deleteChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException {
        dataBaseInterface.deleteChannel(slackChannel);
    }

    @Override
    public SlackChannel getSpecificChannel(String webhook) throws ChannelNotExitsInDataBaseException {
        return dataBaseInterface.getSpecificChannel(webhook);
    }

    @Override
    public ArrayList<?> getChannels(String filter) {
        return dataBaseInterface.getChannels(filter);
    }

}
