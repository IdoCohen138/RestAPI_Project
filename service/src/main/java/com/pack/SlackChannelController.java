package com.pack;

import com.pack.exceptions.ChannelAlreadyExitsInDataBaseException;
import com.pack.exceptions.ChannelNotExitsInDataBaseException;
import com.pack.exceptions.SlackMessageNotSentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SlackChannelController implements Business {

    @Autowired
    @Qualifier("messageSender")
    MessageSender messageSender;
    @Autowired
    Persistent SlackJpaRepository;

    @Override
    public void createChannel(SlackChannel slackChannel) throws ChannelAlreadyExitsInDataBaseException {
        SlackJpaRepository.saveChannel(slackChannel);
        try {
            if (slackChannel.getStatus().equals(EnumStatus.DISABLED)) return;
            String message = "New channel has been created";
            messageSender.sendMessage(slackChannel, message);
        } catch (SlackMessageNotSentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateChannel(UUID id, EnumStatus status) throws ChannelNotExitsInDataBaseException {
        SlackChannel modifyChannel = SlackJpaRepository.updateChannel(id, status);
        try {
            if (modifyChannel.getStatus().equals(EnumStatus.DISABLED)) return;
            String message = "Channel's status has been updated";
            messageSender.sendMessage(modifyChannel, message);
        } catch (SlackMessageNotSentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteChannel(UUID id) throws ChannelNotExitsInDataBaseException {
        SlackChannel slackChannel = SlackJpaRepository.deleteChannel(id);
        if (slackChannel.getStatus() == EnumStatus.DISABLED) return;
        try {
            String message = "Channel has been deleted";
            messageSender.sendMessage(slackChannel, message);
        } catch (SlackMessageNotSentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public SlackChannel getChannel(UUID id) throws ChannelNotExitsInDataBaseException {
        return SlackJpaRepository.getChannelbyID(id);
    }

    @Override
    public List<SlackChannel> getChannels(EnumStatus filter) {
        return SlackJpaRepository.getAllChannelsbyStatus(filter);
    }

    @Override
    public List<SlackChannel> getAllChannels() {
        return SlackJpaRepository.getAllChannels();
    }
}
