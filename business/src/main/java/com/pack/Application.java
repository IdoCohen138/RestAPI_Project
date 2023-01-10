package com.pack;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackageClasses = {LogMessagePrimaryKey.class, LogMessages.class, SlackChannel.class})
@EnableJpaRepositories

public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

		}
}
