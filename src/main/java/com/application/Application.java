package com.application;

import com.application.service.SlackIntegration;
import com.application.service.exceptions.SlackMessageNotSentException;
import com.application.service.SlackChannelController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling

public class Application {

	public static void main(String[] args) throws SlackMessageNotSentException {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		SlackChannelController slackChannelController = context.getBean(SlackChannelController.class);
		slackChannelController.sendPeriodicMessages();

		}

}
