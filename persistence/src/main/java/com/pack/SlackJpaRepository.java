package com.pack;

import com.pack.exceptions.ChannelAlreadyExitsInDataBaseException;
import com.pack.exceptions.ChannelNotExitsInDataBaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class SlackJpaRepository implements Persistent {
    @Autowired
    @Qualifier("ISlackJpaRepository")
    ISlackJpaRepository Repository;

    @Override
    public void saveChannel(SlackChannel slackChannel) throws ChannelAlreadyExitsInDataBaseException {
        try {
            Repository.save(slackChannel);
        } catch (DataIntegrityViolationException e) {
            throw new ChannelAlreadyExitsInDataBaseException("This channel already exits in database");
        }
    }

    @Override
    public SlackChannel updateChannel(UUID id, EnumStatus status) throws ChannelNotExitsInDataBaseException {
        SlackChannel modifyChannel;
        try {
            modifyChannel = Repository.findById(id).get();
            modifyChannel.setStatus(status);
            modifyChannel.setModified_at(new Timestamp(System.currentTimeMillis()));
            Repository.updateChannel(id, status);
        } catch (NoSuchElementException e) {
            throw new ChannelNotExitsInDataBaseException("This channel not exits in the database");
        }
        return modifyChannel;
    }

    @Override
    public SlackChannel deleteChannel(UUID id) throws ChannelNotExitsInDataBaseException {
        SlackChannel slackChannel;
        try {
            slackChannel = Repository.findById(id).get();
            Repository.delete(slackChannel);
        } catch (NoSuchElementException e) {
            throw new ChannelNotExitsInDataBaseException("This channel not exits in the database");
        }
        return slackChannel;
    }

    @Override
    public SlackChannel getChannelbyID(UUID id) throws ChannelNotExitsInDataBaseException {
        SlackChannel slackChannel;
        try {
            slackChannel = Repository.findById(id).get();
        } catch (NoSuchElementException  |InvalidDataAccessApiUsageException e) {
            throw new ChannelNotExitsInDataBaseException("This channel not exits in the database");
        }
        return slackChannel;
    }

    @Override
    public List<SlackChannel> getAllChannelsbyStatus(EnumStatus filter) {
        Specification<SlackChannel> spec = (root, query, builder) -> builder.equal(root.get("status"), filter);
        return Repository.findAll(spec);
    }

    @Override
    public List<SlackChannel> getAllChannels() {
        return Repository.findAll();
    }
}
