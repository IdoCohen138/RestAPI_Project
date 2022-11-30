package com.application.serviceLayer;

import com.application.presentationLayer.Exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.presentationLayer.Exceptions.ChannelNotExitsInDataBaseException;
import com.application.presentationLayer.DataAccess;

import java.io.IOException;
import java.util.ArrayList;

public class SlackChannelController implements ChannelRepository{
    Repository repository = new DataAccess();
    @Override
    public void createChannel(SlackChannel slackChannel) throws ChannelAlreadyExitsInDataBaseException {
        repository.createChannel(slackChannel);
        SlackIntegration si = new SlackIntegration("New channel has been created");
        try{
            si.sendMessage(slackChannel);
        } catch (IOException e) {
            System.out.println("Cant send Slack Message");
        }
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
        SlackIntegration si = new SlackIntegration("Channel's status has been updated");
        try{
            si.sendMessage(slackChannel);
        } catch (IOException e) {
            System.out.println("Message cant sent to Slack");
        }
    }

    @Override
    public void deleteChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException {
        repository.deleteChannel(slackChannel);
        SlackIntegration si = new SlackIntegration("Channel has been deleted");
        try{
            si.sendMessage(slackChannel);
        } catch (IOException e) {
            System.out.println("Message cant sent to Slack");
        }

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
