package com.example.demo.businessLayer;
import com.example.demo.serviceLayer.SlackChannel;
import com.example.demo.serviceLayer.SlackChannelController;
import com.example.demo.serviceLayer.channelRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@RestController
public class endPoints {

    channelRepository channelInterface = new SlackChannelController();

    //TODO: validate parameters in all endpoints

    @PostMapping("/channels")
    public ResponseEntity<String> createChannel(@RequestBody SlackChannel slackChannel){
        channelInterface.createChannel(slackChannel);
        return new ResponseEntity<>("The channel has already exits", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateChannel(@RequestBody SlackChannel slackChannel) {
        if (channelInterface.updateChannel(slackChannel))
            return new ResponseEntity<>("The channel status has been modify successful.", HttpStatus.OK);
        return new ResponseEntity<>("There are no channel with this name.", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/deleted")
    public ResponseEntity<String> deleteChannel(@RequestBody SlackChannel slackChannel){
        if(channelInterface.deleteChannel(slackChannel))
            return new ResponseEntity<>("The channel has been deleted successful.", HttpStatus.OK);
        return new ResponseEntity<>("The channel is not found", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getSpecificChannel")
    public @ResponseBody ResponseEntity<?> getSpecificChannel(@RequestParam  SlackChannel slackChannel){
        SlackChannel channel = channelInterface.getSpecificChannel(slackChannel);
        if (channel!= null)
            return new ResponseEntity<>(channel, HttpStatus.OK);
        return new ResponseEntity<>("There is no channel with this details.", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getChannels")
    public @ResponseBody ResponseEntity<?> getChannels(@RequestParam SlackChannel slackChannel){
        ArrayList channels = channelInterface.getChannels(slackChannel);
        if (channels.size()==0)
            return new ResponseEntity<>("There are no channels to return.", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(channels, HttpStatus.OK);
    }

}
