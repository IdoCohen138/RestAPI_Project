package com.application.serviceLayer;
import com.slack.api.webhook.WebhookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.application.presentationLayer.Exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.presentationLayer.Exceptions.ChannelNotExitsInDataBaseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
@Component("slackcontroller")
public class SlackChannelController implements ChannelRepository{
    @Autowired
    Repository repository;
    SlackIntegration slackIntegration = new SlackIntegration();

    @Override
    public void createChannel(SlackChannel slackChannel) throws ChannelAlreadyExitsInDataBaseException {
        slackChannel.setId(UUID.randomUUID());
        repository.createChannel(slackChannel);
        WebhookResponse response;
        try{
            response =slackIntegration.sendMessage(slackChannel, "New channel has been created");
        } catch (IOException e) {
            System.out.println("Cant send Slack Message");

        }
    }

    @Override
        public void updateChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException {
            SlackChannel modifyChannel = repository.updateChannel(slackChannel);
            modifyChannel.setStatus(slackChannel.getStatus());
            try{
                slackIntegration.sendMessage(modifyChannel,"Channel's status has been updated");//must be modifyChannel!! -> the "slackChannel" object not for sure have webhook but the "modifyChannel" object have webhook..
            } catch (IOException e) {
                System.out.println("Message cant sent to Slack");
            }
    }

    @Override
    public void deleteChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException {
        SlackChannel deleteChannel = repository.deleteChannel(slackChannel);
        try{
            slackIntegration.sendMessage(deleteChannel,"Channel has been deleted");
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
            slackIntegration.sendMessage((SlackChannel)repository.getChannels("ENABLED").get(i),"You have no vulnerabilities");

        }

    }
}
