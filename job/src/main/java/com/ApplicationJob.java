package com;


public class ApplicationJob {

    public static void main(String[] args) {
        SlackIntegration slackIntegration= new SlackIntegration();
        slackIntegration.sendPeriodicMessages();
    }
}
