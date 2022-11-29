package com.example.demo.serviceLayer;

import com.example.demo.presentationLayer.Exceptions.ChannelAlreadyExitsInDataBaseException;
import com.example.demo.presentationLayer.Exceptions.ChannelNotExitsInDataBaseException;
import com.example.demo.serviceLayer.SlackChannel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Map;

public interface repository {


    void createChannel(SlackChannel newChannel) throws ChannelAlreadyExitsInDataBaseException;
    SlackChannel updateChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException;
    void deleteChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException;
    SlackChannel getSpecificChannel(String webhook) throws ChannelNotExitsInDataBaseException;
    ArrayList<?> getChannels(String filter);
}
