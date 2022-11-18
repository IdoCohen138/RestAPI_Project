
package com.example.demo.controllers;
import com.example.demo.Enum;
import com.example.demo.models.SlackChannel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class SlackChannelController {

    private final ArrayList<SlackChannel> channels = new ArrayList<>();

    //TODO: validate parameters in all endpoints


    @PostMapping("/create")
    public ResponseEntity<String> createChannel(@RequestBody Map<String, String> json){
        String webhook = json.get("webhook");
        String channelName = extractChannelNameFromJson(json);
        SlackChannel newChannel = new SlackChannel(webhook, channelName);
        SlackChannel checkIfChannelExist = getChannel(channelName);
        if (checkIfChannelExist==null) {
            channels.add(newChannel);
            return new ResponseEntity<>("The channel has been created successful.", HttpStatus.OK);
        }
        return new ResponseEntity<>("The channel has already exits", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateChannel(@RequestBody Map<String, String> json){
        String channelName = extractChannelNameFromJson(json);
        try {
            SlackChannel modifyChannel = getChannel(channelName);
            if (modifyChannel!=null){
                modifyChannel.setStatus();
            }
            else
                throw new Exception();
        }
        catch (Exception e){
            return new ResponseEntity<>("There are no channel with this name.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("The channel status has been modify successful.", HttpStatus.OK);
    }

    @DeleteMapping("/deleted")
    public ResponseEntity<String> deleteChannel(@RequestBody Map<String, String> json){
        String channelName = extractChannelNameFromJson(json);
        if (deleteChannel(channelName).equals(Enum.operationStatus.success))
            return new ResponseEntity<>("The channel has been deleted successful.", HttpStatus.OK);
        return new ResponseEntity<>("The channel is not found", HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/getSpecificChannel")
    public @ResponseBody ResponseEntity<?> getSpecificChannel(@RequestParam  Map<String, String> json){
        String channelName = extractChannelNameFromJson(json);
        SlackChannel channel = getChannel(channelName);
        if (channel != null)
            return new ResponseEntity<>(channel, HttpStatus.OK);
        return new ResponseEntity<>("There is no channel with this details.", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getChannels")
    public @ResponseBody ResponseEntity<?> getChannels(@RequestParam Map<String, String> json){
        if (channels.size()==0)
            return new ResponseEntity<>("There are no channels to return.", HttpStatus.BAD_REQUEST);

        //assume that have 3 types of filtering: "Enable", "Disable" and "none".
        String filter_Enable_Disable_None = json.get("filtering");
        ArrayList<SlackChannel> filterArray = sortArrayByFiltering(filter_Enable_Disable_None);
        return new ResponseEntity<>(filterArray, HttpStatus.OK);

    }

    private ArrayList<SlackChannel> sortArrayByFiltering(String filter) {
        if (filter.equals("None"))
            return channels;
        ArrayList<SlackChannel> filterArray = new ArrayList<>();
        for (SlackChannel channel : channels) {
            if (channel.getStatus().toString().equals(filter))
                filterArray.add(channel);
        }
        return filterArray;
    }


    public String extractChannelNameFromJson(Map<String, String> json){
        return json.get("channelName");
    }

    public SlackChannel getChannel(String channelName){
        for (SlackChannel modifyChannel : channels) {
            if (channelName.equals(modifyChannel.getChannelName()))
                return modifyChannel;
        }
        return null;
    }

    public Enum.operationStatus deleteChannel(String channelName){
        SlackChannel toRemove = getChannel(channelName);
        if (toRemove != null) {
            channels.remove(toRemove);
            return Enum.operationStatus.success;
        }
        return Enum.operationStatus.failure;
    }
}