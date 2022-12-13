package com.application.business;

import com.application.persistence.exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.persistence.exceptions.ChannelNotExitsInDataBaseException;
import com.application.service.EnumStatus;
import com.application.service.SlackChannel;
import com.application.service.Business;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.UUID;

@RestController
@Validated
public class EndPoints {
    @Autowired
    Business business;

    @PostMapping("/channels")
    public ResponseEntity<String> createChannel(@RequestBody SlackChannel slackChannel) {
        try {
            business.createChannel(slackChannel);
            return new ResponseEntity<>("The channel has created successful.", HttpStatus.OK);
        } catch (ChannelAlreadyExitsInDataBaseException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/channels") //must contain status & id
    public ResponseEntity<String> updateChannel(@RequestBody SlackChannel slackChannel) {
        try {
            business.updateChannel(slackChannel);
            return new ResponseEntity<>("The channel status has been modify successful.", HttpStatus.OK);
        } catch (ChannelNotExitsInDataBaseException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/channels") //must contain id
    public ResponseEntity<String> deleteChannel(@RequestBody SlackChannel slackChannel) {
        try {
            business.deleteChannel(slackChannel);
            return new ResponseEntity<>("The channel has been deleted successful.", HttpStatus.OK);
        } catch (ChannelNotExitsInDataBaseException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/channels", params = "id")  //must contain id in path param
    public @ResponseBody ResponseEntity<?> getSpecificChannel(@PathParam("id") @RequestParam(value = "id") UUID id) {
        try {
            SlackChannel channel = business.getChannel(id);
            return new ResponseEntity<>(channel, HttpStatus.OK);
        } catch (ChannelNotExitsInDataBaseException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/channels", params = "status") //must contain status in path param
    public @ResponseBody ResponseEntity<?> getChannels(@PathParam("status") @RequestParam EnumStatus status) {
        List<SlackChannel> channels = business.getChannels(status);
        return new ResponseEntity<>(channels, HttpStatus.OK);
    }

    @RequestMapping(value = "/channels")
    public @ResponseBody ResponseEntity<?> getChannels() {
        List<SlackChannel> channels = business.getAllChannels();
        if (channels.size() == 0)
            return new ResponseEntity<>("There are no channels to return.", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(channels, HttpStatus.OK);
    }

}
