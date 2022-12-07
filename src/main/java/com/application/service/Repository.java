package com.application.service;

import com.application.persistence.Exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.persistence.Exceptions.ChannelNotExitsInDataBaseException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface Repository {


    void createChannel(SlackChannel newChannel) throws ChannelAlreadyExitsInDataBaseException;

    SlackChannel updateChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException;

    SlackChannel deleteChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException;

    SlackChannel getSpecificChannel(UUID uuid) throws ChannelNotExitsInDataBaseException;

    List<SlackChannel> getChannels(String filter);

    List<SlackChannel> getAllChannels();
}
