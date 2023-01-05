package com.application.service;

import com.application.persistence.exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.persistence.exceptions.ChannelNotExitsInDataBaseException;
import com.application.service.exceptions.SlackMessageNotSentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class SlackChannelController implements Business {
    @Autowired
    MessageSender messageSender;
    @Autowired
    SlackRepository channelSlackRepository;

    @Override
    public void createChannel(SlackChannel slackChannel) throws ChannelAlreadyExitsInDataBaseException {
        try {
            channelSlackRepository.save(slackChannel);
        } catch (DataIntegrityViolationException e) {
            throw new ChannelAlreadyExitsInDataBaseException("This channel already exits in database");
        }
        try {
            if (slackChannel.getStatus().equals(EnumStatus.DISABLED))
                return;
            String message = "New channel has been created";
            messageSender.sendMessage(slackChannel, message);
        } catch (SlackMessageNotSentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateChannel(UUID id, EnumStatus status) throws ChannelNotExitsInDataBaseException {
        SlackChannel modifyChannel;
        try {
            modifyChannel = channelSlackRepository.findById(id).get();
            modifyChannel.setStatus(status);
            modifyChannel.setModified_at(new Timestamp(System.currentTimeMillis()));
            channelSlackRepository.updateChannel(id, status);
        } catch (NoSuchElementException e) {
            throw new ChannelNotExitsInDataBaseException("This channel not exits in the database");
        }
        try {
            if (modifyChannel.getStatus().equals(EnumStatus.DISABLED))
                return;
            String message = "Channel's status has been updated";
            messageSender.sendMessage(modifyChannel, message);
        } catch (SlackMessageNotSentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteChannel(UUID id) throws ChannelNotExitsInDataBaseException {
        SlackChannel slackChannel;
        try {
            slackChannel = channelSlackRepository.findById(id).get();
            slackChannel.getLogMessages().clear();
            channelSlackRepository.delete(slackChannel);
        } catch (NoSuchElementException e) {
            throw new ChannelNotExitsInDataBaseException("This channel not exits in the database");
        }

        if (slackChannel.getStatus() == EnumStatus.DISABLED)
            return;
        try {
            String message = "Channel has been deleted";
            messageSender.sendMessage(slackChannel, message);
        } catch (SlackMessageNotSentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public SlackChannel getChannel(UUID id) throws ChannelNotExitsInDataBaseException {
        SlackChannel slackChannel;
        try {
            slackChannel = channelSlackRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new ChannelNotExitsInDataBaseException("This channel not exits in the database");
        }

        return slackChannel;
    }

    @Override
    public List<SlackChannel> getChannels(EnumStatus filter) {
        Specification<SlackChannel> spec = (root, query, builder) ->
                builder.equal(root.get("status"), filter);
        return channelSlackRepository.findAll(spec);
    }

    @Override
    public List<SlackChannel> getAllChannels() {
        return channelSlackRepository.findAll();
    }
}
