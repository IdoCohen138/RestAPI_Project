package com.application.service;

import com.application.persistence.Exceptions.SlackMessageNotSentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.application.persistence.Exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.persistence.Exceptions.ChannelNotExitsInDataBaseException;

import java.util.List;
import java.util.UUID;

@Component("slack-controller")
public class SlackChannelController implements ChannelRepository {
    @Autowired
    Repository repository;
    SlackIntegration slackIntegration = new SlackIntegration();

    @Override
    public void createChannel(SlackChannel slackChannel) throws ChannelAlreadyExitsInDataBaseException {
        slackChannel.setId(UUID.randomUUID());
        repository.createChannel(slackChannel);

        try {
            slackIntegration.sendMessage(slackChannel, "New channel has been created");
        } catch (SlackMessageNotSentException e) {
            System.out.println("Cant send Slack Message");

        }
    }

    @Override
    public void updateChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException {
        SlackChannel modifyChannel = repository.updateChannel(slackChannel);
        modifyChannel.setStatus(slackChannel.getStatus());
        try {
            slackIntegration.sendMessage(modifyChannel, "Channel's status has been updated");//must be modifyChannel!! -> the "slackChannel" object not for sure have webhook but the "modifyChannel" object have webhook..
        } catch (SlackMessageNotSentException e) {
            System.out.println("Message cant sent to Slack");
        }
    }

    @Override
    public void deleteChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException {
        SlackChannel deleteChannel = repository.deleteChannel(slackChannel);
        try {
            slackIntegration.sendMessage(deleteChannel, "Channel has been deleted");
        } catch (SlackMessageNotSentException e) {
            System.out.println("Message cant sent to Slack");
        }
    }

    @Override
    public SlackChannel getSpecificChannel(UUID uuid) throws ChannelNotExitsInDataBaseException {
        return repository.getSpecificChannel(uuid);
    }

    @Override
    public List<SlackChannel> getChannels(EnumStatus filter) {
        return repository.getChannels(String.valueOf(filter));
    }

    @Override
    public List<SlackChannel> getAllChannels() {
        return repository.getAllChannels();
    }

    @Scheduled(cron = "0 0 10 * * *")
    public void sendPeriodicMessages() throws SlackMessageNotSentException {
        for (int i = 0; i < repository.getChannels("ENABLED").size(); i++) {
            slackIntegration.sendMessage((SlackChannel) repository.getChannels("ENABLED").get(i), "You have no vulnerabilities");

        }

    }
}
