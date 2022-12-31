package com.application.service;

import com.application.persistence.exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.persistence.exceptions.ChannelNotExitsInDataBaseException;
import com.application.service.exceptions.SlackMessageNotSentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class SlackChannelController implements Business {

    SlackRepository channelSlackRepository;

    @Autowired
    SlackIntegration slackIntegration;

    @Autowired
    public SlackChannelController(SlackRepository channelSlackRepository) {
        this.channelSlackRepository = channelSlackRepository;
    }

    @Override
    public void createChannel(SlackChannel slackChannel) throws ChannelAlreadyExitsInDataBaseException {
        if (slackChannel.getStatus() != null && slackChannel.getStatus().equals(EnumStatus.DISABLED))
            slackChannel.setStatus(EnumStatus.DISABLED);
        else
            slackChannel.setStatus(EnumStatus.ENABLED);
        try {
            channelSlackRepository.save(slackChannel);
        } catch (DataIntegrityViolationException | EntityNotFoundException | InvalidDataAccessApiUsageException e) {
            throw new ChannelAlreadyExitsInDataBaseException("This channel already exits in database");
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
    public void updateChannel(UUID id, EnumStatus status) throws ChannelNotExitsInDataBaseException {
        SlackChannel modifyChannel;
        try {
            modifyChannel = channelSlackRepository.findById(id).get();
            modifyChannel.setStatus(status);
            modifyChannel.setModified_at(new Timestamp(System.currentTimeMillis()));
            channelSlackRepository.updateChannel(id, status);
        } catch (DataIntegrityViolationException | EntityNotFoundException | InvalidDataAccessApiUsageException e) {
            throw new ChannelNotExitsInDataBaseException("This channel not exits in the database");
        }
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
        SlackChannel slackChannel;
        try {
            slackChannel = channelSlackRepository.findById(id).get();
            channelSlackRepository.delete(slackChannel);
        } catch (DataIntegrityViolationException | EntityNotFoundException | NoSuchElementException |
                 InvalidDataAccessApiUsageException e) {
            throw new ChannelNotExitsInDataBaseException("This channel not exits in the database");
        }

        if (slackChannel.getStatus() == EnumStatus.DISABLED)
            return;
        try {
            slackIntegration.sendMessage(slackChannel, "Channel has been deleted");
        } catch (SlackMessageNotSentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public SlackChannel getChannel(UUID id) throws ChannelNotExitsInDataBaseException {
        SlackChannel slackChannel;
        try {
            slackChannel = channelSlackRepository.findById(id).get();
        } catch (DataIntegrityViolationException | EntityNotFoundException | InvalidDataAccessApiUsageException e) {
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

    @Scheduled(cron = "0 0 10 * * *")
    public void sendPeriodicMessages() {
        for (SlackChannel slackChannel : getChannels(EnumStatus.ENABLED)) {
            try {
                slackIntegration.sendMessage(slackChannel, "You have no vulnerabilities");
            } catch (SlackMessageNotSentException slackMessageNotSentException) {
                System.out.println(slackMessageNotSentException.getMessage());
            }
        }
    }
}
