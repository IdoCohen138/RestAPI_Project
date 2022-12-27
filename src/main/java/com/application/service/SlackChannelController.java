package com.application.service;

import com.application.service.exceptions.SlackMessageNotSentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.application.persistence.exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.persistence.exceptions.ChannelNotExitsInDataBaseException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component("slackcontroller")
public class SlackChannelController implements Business {
    @Autowired
    Repository channelRepository;

    @Autowired
    SlackIntegration slackIntegration;

    @Override
    public void createChannel(SlackChannel slackChannel_) throws ChannelAlreadyExitsInDataBaseException {
        slackChannel_.setId(UUID.randomUUID());
        if (slackChannel_.getStatus()!=null && slackChannel_.getStatus().equals(EnumStatus.DISABLED))
            slackChannel_.setStatus(EnumStatus.DISABLED);
        else
            slackChannel_.setStatus(EnumStatus.ENABLED);
        channelRepository.createChannel(slackChannel_);
        try {
            if (slackChannel_.getStatus().equals(EnumStatus.DISABLED))
                return;
            slackIntegration.sendMessage(slackChannel_, "New channel has been created");
        } catch (SlackMessageNotSentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateChannel(UUID id, String status) throws ChannelNotExitsInDataBaseException {
        SlackChannel modifyChannel = getChannel(id);
        modifyChannel.setStatus(status);
        try {
            if (modifyChannel.getStatus().equals(EnumStatus.DISABLED))
                return;
            slackIntegration.sendMessage(modifyChannel, "Channel's status has been updated");
        } catch (SlackMessageNotSentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteChannel(UUID id) throws ChannelNotExitsInDataBaseException {
        SlackChannel slackChannel=getChannel(id);
        boolean status=true;
        if (slackChannel!=null && Objects.equals(slackChannel.getStatus(), "DISABLED")) status=false;
        try {
            channelRepository.deleteChannel(id);
            if (!status)
                return;
            slackIntegration.sendMessage(slackChannel, "Channel has been deleted");
        } catch (SlackMessageNotSentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public SlackChannel getChannel(UUID uuid) throws ChannelNotExitsInDataBaseException {
        return channelRepository.getChannel(uuid);
    }

    @Override
    public List<SlackChannel> getChannels(String filter) {
        return channelRepository.getChannels(filter);
    }
        @Override
    public List<SlackChannel> getAllChannels() {
        return channelRepository.getAllChannels();
    }

//    @Scheduled(cron = "0 0 10 * * *")
//    public void sendPeriodicMessages() {
//        for (SlackChannel slackChannel: channelRepository.getChannels(EnumStatus.ENABLED)) {
//            try{
//                slackIntegration.sendMessage(slackChannel, "You have no vulnerabilities");
//            }
//            catch (SlackMessageNotSentException slackMessageNotSentException) {
//                System.out.println(slackMessageNotSentException.getMessage());
//            }
//        }
//    }
}
