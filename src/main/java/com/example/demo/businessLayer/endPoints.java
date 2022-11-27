package com.example.demo.businessLayer;
import com.example.demo.presentationLayer.Exceptions.ChannelAlreadyExitsInDataBaseException;
import com.example.demo.presentationLayer.Exceptions.ChannelNotExitsInDataBaseException;
import com.example.demo.serviceLayer.SlackChannel;
import com.example.demo.serviceLayer.SlackChannelController;
import com.example.demo.serviceLayer.channelRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.Map;

@RestController
@Validated
public class endPoints {

    channelRepository channelInterface = new SlackChannelController();

    @PostMapping("/channels")
    public ResponseEntity<String> createChannel(@Valid @RequestBody SlackChannel slackChannel){
        try{
            channelInterface.createChannel(slackChannel);
            return new ResponseEntity<>("The channel has created successful.", HttpStatus.OK);
        }
        catch (ChannelAlreadyExitsInDataBaseException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/channels")
    public ResponseEntity<String> updateChannel(@Valid @RequestBody SlackChannel slackChannel) {
        try{
            channelInterface.updateChannel(slackChannel);
            return new ResponseEntity<>("The channel status has been modify successful.", HttpStatus.OK);
        }
        catch (ChannelNotExitsInDataBaseException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/channels")
    public ResponseEntity<String> deleteChannel(@Valid @RequestBody SlackChannel slackChannel){
        try{
            channelInterface.deleteChannel(slackChannel);
            return new ResponseEntity<>("The channel has been deleted successful.", HttpStatus.OK);
        }
        catch (ChannelNotExitsInDataBaseException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value="/channels", params = "webhook")
    public @ResponseBody ResponseEntity<?> getSpecificChannel(@PathParam("webhook") @RequestParam String webhook){

        try{
            SlackChannel channel = channelInterface.getSpecificChannel(webhook);
            return new ResponseEntity<>(channel, HttpStatus.OK);
        }
        catch (ChannelNotExitsInDataBaseException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/channels", params = "status")
    public @ResponseBody ResponseEntity<?> getChannels(@PathParam("status") @RequestParam String status){
        ArrayList<?> channels = channelInterface.getChannels(status);
        if (channels.size()==0)
            return new ResponseEntity<>("There are no channels to return.", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(channels, HttpStatus.OK);
    }

}
