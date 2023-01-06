package com;


import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class ApplicationJob {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        SlackIntegration slackIntegration = context.getBean(SlackIntegration.class);
        slackIntegration.sendPeriodicMessages();
    }
}
