package com.application.business;

import com.application.persistence.exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.persistence.exceptions.ChannelNotExitsInDataBaseException;
import com.application.service.EnumStatus;
import com.application.service.SlackChannel;
import com.application.service.Business;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.UUID;

@RestController
public class EndPoints {
    @Autowired
    Business business;

    @PostMapping("/channels")
    public ResponseEntity<String> createChannel(@Valid @RequestBody SlackChannel slackChannel) {
        try {
            business.createChannel(slackChannel);
            return new ResponseEntity<>("The channel has created successful.", HttpStatus.OK);
        } catch (ChannelAlreadyExitsInDataBaseException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/channels/{id}")
    public ResponseEntity<String> updateChannel(@PathVariable UUID id, @RequestBody SlackChannel status) {
        try {
            business.updateChannel(id, status.getStatus());
            return new ResponseEntity<>("The channel status has been modify successful.", HttpStatus.OK);
        } catch (ChannelNotExitsInDataBaseException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/channels/{id}")
    public ResponseEntity<String> deleteChannel(@PathVariable UUID id) {
        try {
            business.deleteChannel(id);
            return new ResponseEntity<>("The channel has been deleted successful.", HttpStatus.OK);
        } catch (ChannelNotExitsInDataBaseException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/channels/{id}")
    public @ResponseBody ResponseEntity<?> getSpecificChannel(@PathVariable UUID id) {
        try {
            SlackChannel channel = business.getChannel(id);
            return new ResponseEntity<>(channel, HttpStatus.OK);
        } catch (ChannelNotExitsInDataBaseException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/channels", params = "status")
    public @ResponseBody ResponseEntity<?> getChannels(@PathParam("status") @RequestParam EnumStatus status) {
        List<SlackChannel> channels = business.getChannels(status);
        return new ResponseEntity<>(channels, HttpStatus.OK);
    }

    @GetMapping(value = "/channels")
    public @ResponseBody ResponseEntity<?> getChannels() {
        List<SlackChannel> channels = business.getAllChannels();
        return new ResponseEntity<>(channels, HttpStatus.OK);
    }

}
