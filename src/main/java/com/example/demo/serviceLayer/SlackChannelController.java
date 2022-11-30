package com.example.demo.serviceLayer;

import com.example.demo.presentationLayer.Exceptions.ChannelAlreadyExitsInDataBaseException;
import com.example.demo.presentationLayer.Exceptions.ChannelNotExitsInDataBaseException;
import com.example.demo.presentationLayer.dataBase;

import java.io.IOException;
import java.util.ArrayList;

public class SlackChannelController implements channelRepository{
    repository dataBaseInterface = new dataBase();
    @Override
    public void createChannel(SlackChannel slackChannel) throws ChannelAlreadyExitsInDataBaseException {
        dataBaseInterface.createChannel(slackChannel);
        SlackIntegration si = new SlackIntegration("New channel has been created");
        try{
            si.sendMessage(slackChannel);
        } catch (IOException e) {
            System.out.println("Cant send Slack Message");
        }
    }

    @Override
    public void updateChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException {
        SlackChannel modifyChannel = dataBaseInterface.updateChannel(slackChannel);
        modifyChannel.setStatus();
        SlackIntegration si = new SlackIntegration("Channel's status has been updated");
        try{
            si.sendMessage(slackChannel);
        } catch (IOException e) {
            System.out.println("Message cant sent to Slack");
        }
    }

    @Override
    public void deleteChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException {
        dataBaseInterface.deleteChannel(slackChannel);
        SlackIntegration si = new SlackIntegration("Channel has been deleted");
        try{
            si.sendMessage(slackChannel);
        } catch (IOException e) {
            System.out.println("Message cant sent to Slack");
        }

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
