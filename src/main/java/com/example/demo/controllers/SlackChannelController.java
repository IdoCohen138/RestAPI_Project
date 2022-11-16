
package com.example.demo.controllers;

import com.example.demo.models.SlackChannel;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
public class SlackChannelController {

    private final ArrayList<SlackChannel> channels = new ArrayList<>();

    @PostMapping("/create")
    public String createChannel(@RequestBody Map<String, String> json){
        String webhook = json.get("webhook");
        String channelName = json.get("channelName");
        SlackChannel newChannel = new SlackChannel(webhook, channelName);
        channels.add(newChannel);
        return "i";
    }

    @PutMapping("/update")
    public String updateChannel(@RequestBody Map<String, String> json){
        String channelName = json.get("channelName");
        try {
            for (SlackChannel modifyChannel : channels) {
                if (channelName.equals(modifyChannel.getChannelName()))
                    modifyChannel.setStatus();
            }
        }
        catch (Exception e){
            return "There are no channel with this name.";
        }
        return "The channel status has been modify successful.";
    }

}