package com.example.demo;

import com.example.demo.businessLayer.SlackChannelController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class Client {
    private SlackChannelController slackChannelController;

    public Client(SlackChannelController slackChannelController) {
        this.slackChannelController = slackChannelController;
    }

    public boolean CreateChannelTest(Map<String, String> json) {
        ResponseEntity<String> return_ = new ResponseEntity<String>("The channel has been created successful.", HttpStatus.OK);
        if (return_.equals(slackChannelController.createChannel(json))) {
            return true;
        }

        return false;
    }
    public boolean updateChannelTest(Map<String, String> json) {
        ResponseEntity<String> return_ = new ResponseEntity<String>("TThe channel status has been modify successful.", HttpStatus.OK);
        if (return_.equals(slackChannelController.updateChannel(json))) {
            return true;
        }

        return false;
    }
    public boolean deleteChannelTest(Map<String, String> json) {
        ResponseEntity<String> return_ = new ResponseEntity<String>("The channel has been deleted successful.", HttpStatus.OK);
        if (return_.equals(slackChannelController.deleteChannel(json))) {
            return true;
        }

        return false;
    }
    public boolean getSpecificChannelTest(Map<String, String> json) {
        ResponseEntity<String> return_ = new ResponseEntity<String>("There is no channel with this details.", HttpStatus.OK);
        if (return_.equals(slackChannelController.createChannel(json))) {
            return false;
        }

        return true;
    }


}