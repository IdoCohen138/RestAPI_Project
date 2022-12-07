package com.application.service;

import com.application.persistence.exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.persistence.exceptions.ChannelNotExitsInDataBaseException;

import java.util.ArrayList;
import java.util.UUID;

public interface ChannelRepository {

    void createChannel(SlackChannel slackChannel) throws ChannelAlreadyExitsInDataBaseException;

    void updateChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException;

    void deleteChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException;

    SlackChannel getSpecificChannel(UUID uuid) throws ChannelNotExitsInDataBaseException;

    ArrayList<?> getChannels(String filter);

    ArrayList<?> getAllChannels();

}
