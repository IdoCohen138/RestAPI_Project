package com.pack;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication()
@EntityScan(basePackageClasses = {LogMessagePrimaryKey.class, LogMessages.class, SlackChannel.class})

public class ApplicationJob {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ApplicationJob.class, args);
//        SlackIntegration slackIntegration = context.getBean(SlackIntegration.class);
//        slackIntegration.sendPeriodicMessages();
    }
}
