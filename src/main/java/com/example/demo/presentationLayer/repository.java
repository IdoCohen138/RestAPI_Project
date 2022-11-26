package com.example.demo.presentationLayer;

import com.example.demo.serviceLayer.SlackChannel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Map;

public interface repository {


    void createChannel(SlackChannel newChannel);
    SlackChannel updateChannel(SlackChannel slackChannel);
    Boolean deleteChannel(SlackChannel slackChannel);
    SlackChannel getSpecificChannel(SlackChannel slackChannel);
    ArrayList getChannels(SlackChannel slackChannel);
}
