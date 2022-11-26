package com.example.demo.serviceLayer;
import java.util.ArrayList;

public interface channelRepository {

    void createChannel(SlackChannel slackChannel);
    Boolean updateChannel(SlackChannel slackChannel);
    Boolean deleteChannel(SlackChannel slackChannel);
    SlackChannel getSpecificChannel(SlackChannel slackChannel);
    ArrayList getChannels(SlackChannel slackChannel);
}
