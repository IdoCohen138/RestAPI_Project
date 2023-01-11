package com;
//import com.application.job.com.SlackIntegration;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
   SpringApplication.run(Application.class, args);
//        SlackIntegration slackIntegration = context.getBean(com.SlackIntegration.class);
//        slackIntegration.sendPeriodicMessages();
    }
}