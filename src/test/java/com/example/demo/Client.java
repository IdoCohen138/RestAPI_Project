package com.example.demo;

import controllers.SlackChannelController;

public class Client {
    private SlackChannelController slackChannelController;

    public Client(SlackChannelController slackChannelController) {
        this.slackChannelController = slackChannelController;
    }

    public boolean CreateChannel(String channelName, String webhook){

    return slackChannelController.CreateChannel(channelName,webhook);
}

}
