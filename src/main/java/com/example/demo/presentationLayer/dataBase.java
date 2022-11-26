package com.example.demo.presentationLayer;
import com.example.demo.Enum;
import com.example.demo.serviceLayer.SlackChannel;
import java.util.ArrayList;

public class dataBase implements repository {

    private final ArrayList<SlackChannel> channels = new ArrayList<>();

    @Override
    public void createChannel(SlackChannel newChannel) {
        SlackChannel checkIfChannelExist = getChannel(newChannel);
        if (checkIfChannelExist==null) {
            channels.add(newChannel);
        }
    }

    @Override
    public SlackChannel updateChannel( SlackChannel slackChannel) {
        return getChannel(slackChannel);
    }

    @Override
    public Boolean deleteChannel(SlackChannel slackChannel) {
            if (deleteChannelFromData(slackChannel).equals(Enum.operationStatus.success))
                return true;
            return false;
        }

    @Override
    public SlackChannel getSpecificChannel(SlackChannel slackChannel) {
        return getChannel(slackChannel);
    }

    @Override
    public ArrayList getChannels(SlackChannel slackChannel) {
        return sortArrayByFiltering(slackChannel);
    }

    public SlackChannel getChannel(SlackChannel channel){
        for (SlackChannel modifyChannel : channels) {
            if (channel.equals(modifyChannel))
                return modifyChannel;
        }
        return null;
    }

    public Enum.operationStatus deleteChannelFromData(SlackChannel slackChannel){
        SlackChannel toRemove = getChannel(slackChannel);
        if (toRemove != null) {
            channels.remove(toRemove);
            return Enum.operationStatus.success;
        }
        return Enum.operationStatus.failure;
    }

    public ArrayList<SlackChannel> sortArrayByFiltering(SlackChannel filterChannel) {
        String filter = filterChannel.getStatus().toString();
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
