package com.application.businessLayer;
import com.application.presentationLayer.Exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.presentationLayer.Exceptions.ChannelNotExitsInDataBaseException;
import com.application.serviceLayer.SlackChannel;
import com.application.serviceLayer.SlackChannelController;
import com.application.serviceLayer.ChannelRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.ArrayList;

@RestController
@Validated
public class EndPoints {

    ChannelRepository channelRepository = new SlackChannelController();

    @PostMapping("/channels")
    public ResponseEntity<String> createChannel(@Valid @RequestBody SlackChannel slackChannel){
        try{
            channelRepository.createChannel(slackChannel);
            return new ResponseEntity<>("The channel has created successful.", HttpStatus.OK);
        }
        catch (ChannelAlreadyExitsInDataBaseException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/channels")
    public ResponseEntity<String> updateChannel(@Valid @RequestBody SlackChannel slackChannel) {
        try{
            channelRepository.updateChannel(slackChannel);
            return new ResponseEntity<>("The channel status has been modify successful.", HttpStatus.OK);
        }
        catch (ChannelNotExitsInDataBaseException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/channels")
    public ResponseEntity<String> deleteChannel(@Valid @RequestBody SlackChannel slackChannel){
        try{
            channelRepository.deleteChannel(slackChannel);
            return new ResponseEntity<>("The channel has been deleted successful.", HttpStatus.OK);
        }
        catch (ChannelNotExitsInDataBaseException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value="/channels", params = "webhook")
    public @ResponseBody ResponseEntity<?> getSpecificChannel(@PathParam("webhook") @RequestParam String webhook){

        try{
            SlackChannel channel = channelRepository.getSpecificChannel(webhook);
            return new ResponseEntity<>(channel, HttpStatus.OK);
        }
        catch (ChannelNotExitsInDataBaseException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/channels", params = "status")
    public @ResponseBody ResponseEntity<?> getChannels(@PathParam("status") @RequestParam String status){
        ArrayList<?> channels = channelRepository.getChannels(status);
        if (channels.size()==0)
            return new ResponseEntity<>("There are no channels to return.", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(channels, HttpStatus.OK);
    }

}
