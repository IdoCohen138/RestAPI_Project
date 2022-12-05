package com.application;

import com.application.serviceLayer.Exceptions.SlackMessageNotSentException;
import com.application.serviceLayer.SlackChannelController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling

public class Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		SlackChannelController emp = context.getBean(SlackChannelController.class);
		try {
			emp.sendPeriodicMessages();

		}catch(SlackMessageNotSentException e) {
			System.out.println("Message cant sent to Slack");
		}
		}

}
