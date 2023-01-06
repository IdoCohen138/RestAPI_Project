package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EntityScan(basePackageClasses = {com.LogMessagePrimaryKey.class, com.LogMessages.class, com.SlackChannel.class})
@EnableJpaRepositories
public class ApplicationJob {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationJob.class, args);
        ConfigurableApplicationContext context = SpringApplication.run(ApplicationJob.class, args);
        SlackIntegration slackIntegration = context.getBean(SlackIntegration.class);
        slackIntegration.sendPeriodicMessages();
    }
}
