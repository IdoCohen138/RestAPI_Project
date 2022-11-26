package com.example.demo.serviceLayer;

import com.example.demo.presentationLayer.dataBase;
import com.example.demo.presentationLayer.repository;
import java.util.ArrayList;

public class SlackChannelController implements channelRepository{
    repository dataBaseInterface = new dataBase();

    @Override
    public void createChannel(SlackChannel slackChannel) {
        dataBaseInterface.createChannel(slackChannel);
    }

    public Boolean updateChannel(SlackChannel slackChannel){
        try {
           SlackChannel modifyChannel= dataBaseInterface.updateChannel(slackChannel);
            if (modifyChannel!=null){
                modifyChannel.setStatus();
            }
            else
                throw new Exception();
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public Boolean deleteChannel(SlackChannel slackChannel) {
        return dataBaseInterface.deleteChannel(slackChannel);
    }

    @Override
    public SlackChannel getSpecificChannel(SlackChannel slackChannel) {
        return dataBaseInterface.getSpecificChannel(slackChannel);
    }

    @Override
    public ArrayList getChannels(SlackChannel slackChannel) {
        return dataBaseInterface.getChannels(slackChannel);
    }

}
