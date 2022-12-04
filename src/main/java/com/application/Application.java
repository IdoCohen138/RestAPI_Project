package com.application;

import com.application.presentationLayer.DataAccess;
import com.application.presentationLayer.Exceptions.SlackMessageNotSentException;
import com.application.serviceLayer.Repository;
import com.application.serviceLayer.SlackChannelController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
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
