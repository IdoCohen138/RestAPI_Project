package com.application.job;

import com.slack.api.Slack;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class SlackConfiguration {

    @Bean
    public Slack slack() {
        return Slack.getInstance();
    }
}