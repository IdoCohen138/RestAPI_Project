package com.application;

import com.application.job.SlackIntegration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling

public class Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		SlackIntegration slackIntegration = context.getBean(SlackIntegration.class);
		slackIntegration.sendPeriodicMessages();
		}
}
