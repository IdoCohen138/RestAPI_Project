package com.application.serviceLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.application.presentationLayer.Exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.presentationLayer.Exceptions.ChannelNotExitsInDataBaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
@Component("slackcontroller")
public class SlackChannelController implements ChannelRepository{
    @Autowired
    Repository repository;

    @Override
    public void createChannel(SlackChannel slackChannel) throws ChannelAlreadyExitsInDataBaseException {
        slackChannel.setId(UUID.randomUUID());
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
        modifyChannel.setStatus(slackChannel.getStatus());
        SlackIntegration si = new SlackIntegration("Channel's status has been updated");
        try{
            si.sendMessage(modifyChannel);//must be modifyChannel!! -> the "slackChannel" object not for sure have webhook but the "modifyChannel" object have webhook..
        } catch (IOException e) {
            System.out.println("Message cant sent to Slack");
        }
    }

    @Override
    public void deleteChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException {
        SlackChannel deleteChannel = repository.deleteChannel(slackChannel);
        SlackIntegration si = new SlackIntegration("Channel has been deleted");
        try{
            si.sendMessage(deleteChannel);
        } catch (IOException e) {
            System.out.println("Message cant sent to Slack");
        }
    }

    @Override
    public SlackChannel getSpecificChannel(UUID uuid) throws ChannelNotExitsInDataBaseException {
        return repository.getSpecificChannel(uuid);
    }

    @Override
    public ArrayList<?> getChannels(String filter) {
        return repository.getChannels(filter);
    }

    @Override
    public ArrayList<?> getAllChannels() {
        return repository.getAllChannels();
    }
    @Scheduled(cron ="0 0 10 * * *")
    public void sendPeriodicMessages() throws IOException {
        for(int i=0;i<repository.getChannels("ENABLED").size();i++){
            SlackIntegration slackIntegration = new SlackIntegration("You have no vulnerabilities");
            slackIntegration.sendMessage((SlackChannel)repository.getChannels("ENABLED").get(i));

        }

    }
}
