package com.application.businessLayer;
import com.application.presentationLayer.Exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.presentationLayer.Exceptions.ChannelNotExitsInDataBaseException;
import com.application.serviceLayer.SlackChannel;
import com.application.serviceLayer.SlackChannelController;
import com.application.serviceLayer.ChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.UUID;

@RestController
@Validated
public class EndPoints {
    @Autowired
    ChannelRepository channelRepository;

    @PostMapping("/channels")
    public ResponseEntity<String> createChannel(@RequestBody SlackChannel slackChannel){
        try{
            channelRepository.createChannel(slackChannel);
            return new ResponseEntity<>("The channel has created successful.", HttpStatus.OK);
        }
        catch (ChannelAlreadyExitsInDataBaseException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/channels") //must contain status & id
    public ResponseEntity<String> updateChannel(@RequestBody SlackChannel slackChannel) {
        try{
            channelRepository.updateChannel(slackChannel);
            return new ResponseEntity<>("The channel status has been modify successful.", HttpStatus.OK);
        }
        catch (ChannelNotExitsInDataBaseException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/channels") //must contain id
    public ResponseEntity<String> deleteChannel(@RequestBody SlackChannel slackChannel){
        try{
            channelRepository.deleteChannel(slackChannel);
            return new ResponseEntity<>("The channel has been deleted successful.", HttpStatus.OK);
        }
        catch (ChannelNotExitsInDataBaseException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value="/channels", params = "id")  //must contain id in path param
    public @ResponseBody ResponseEntity<?> getSpecificChannel(@PathParam("id") @RequestParam(value = "id") UUID id){
        try{
            SlackChannel channel = channelRepository.getSpecificChannel(id);
            return new ResponseEntity<>(channel, HttpStatus.OK);
        }
        catch (ChannelNotExitsInDataBaseException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/channels", params = "status") //must contain status in path param
    public @ResponseBody ResponseEntity<?> getChannels(@PathParam("status") @RequestParam String status){
        ArrayList<?> channels = channelRepository.getChannels(status);
        if (channels.size()==0)
            return new ResponseEntity<>("There are no channels to return.", HttpStatus.OK);
        return new ResponseEntity<>(channels, HttpStatus.OK);
    }

    @RequestMapping(value="/channels")
    public @ResponseBody ResponseEntity<?> getChannels(){
        ArrayList<?> channels = channelRepository.getAllChannels();
        if (channels.size()==0)
            return new ResponseEntity<>("There are no channels to return.", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(channels, HttpStatus.OK);
    }

}
