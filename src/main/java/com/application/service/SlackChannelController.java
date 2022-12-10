package com.application.service;

import com.application.service.exceptions.SlackMessageNotSentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.application.persistence.exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.persistence.exceptions.ChannelNotExitsInDataBaseException;

import java.util.List;
import java.util.UUID;

@Component("slackcontroller")
public class SlackChannelController implements BusinessInterface {
    @Autowired
    PersistenceInterface persistenceInterface;

    @Autowired
    SlackIntegration slackIntegration;

    @Override
    public void createChannel(SlackChannel slackChannel)  {
        slackChannel.setId(UUID.randomUUID());
        try {
            persistenceInterface.createChannel(slackChannel);
        }
        catch (ChannelAlreadyExitsInDataBaseException e){
            System.out.println(e.getMessage());

        }
        try {
            if (slackChannel.getStatus().equals(EnumStatus.DISABLED))
                return;
            slackIntegration.sendMessage(slackChannel, "New channel has been created");
        } catch (SlackMessageNotSentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateChannel(SlackChannel slackChannel)  {
        try {
            SlackChannel modifyChannel = persistenceInterface.updateChannel(slackChannel);
            if (modifyChannel==null) return;
            modifyChannel.setStatus(slackChannel.getStatus());
        try {
            if (slackChannel.getStatus().equals(EnumStatus.DISABLED))
                return;
            slackIntegration.sendMessage(modifyChannel, "Channel's status has been updated");
        } catch (SlackMessageNotSentException e) {
            System.out.println(e.getMessage());
        }

        }catch (ChannelNotExitsInDataBaseException e) {
            System.out.println(e.getMessage());

        }
    }

    @Override
    public void deleteChannel(SlackChannel slackChannel){
        try {
            SlackChannel deleteChannel = persistenceInterface.deleteChannel(slackChannel);
        if (deleteChannel==null) return;

        try {
            if (slackChannel.getStatus().equals(EnumStatus.DISABLED))
                return;
            slackIntegration.sendMessage(deleteChannel, "Channel has been deleted");
        } catch (SlackMessageNotSentException e) {
            System.out.println(e.getMessage());
        }
        }catch (ChannelNotExitsInDataBaseException e) {
            System.out.println(e.getMessage());

        }
    }


    @Override
    public SlackChannel getChannel(UUID uuid) throws ChannelNotExitsInDataBaseException {
        return persistenceInterface.getChannel(uuid);
    }

    @Override
    public List<SlackChannel> getChannels(EnumStatus filter) {
        return persistenceInterface.getChannels(filter);
    }

    @Override
    public List<SlackChannel> getAllChannels() {
        return persistenceInterface.getAllChannels();
    }

    @Scheduled(cron = "0 0 10 * * *")
    public void sendPeriodicMessages() {
        for (SlackChannel slackChannel: persistenceInterface.getChannels(EnumStatus.ENABLED)) {
            try{
                slackIntegration.sendMessage(slackChannel, "You have no vulnerabilities");
            }
            catch (SlackMessageNotSentException slackMessageNotSentException) {
                System.out.println(slackMessageNotSentException.getMessage());
            }
        }
    }
}
