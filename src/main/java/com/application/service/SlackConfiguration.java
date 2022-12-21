package com.application.service;

import com.slack.api.Slack;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

//@Configuration
@Service
public class SlackConfiguration {

    @Bean
    public Slack slack() {
        return Slack.getInstance();
    }
}