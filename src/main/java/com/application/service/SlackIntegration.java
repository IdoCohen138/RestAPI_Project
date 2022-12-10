package com.application.service;

import com.application.service.exceptions.SlackMessageNotSentException;
import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
public class SlackIntegration {

    Slack slack = Slack.getInstance();

    public WebhookResponse sendMessage(SlackChannel slackChannel, String message) throws SlackMessageNotSentException {
        Payload payload = Payload.builder().text(message).build();
        String webhookUrl = slackChannel.getWebhook();
        try {
            WebhookResponse response = slack.send(webhookUrl, payload);
            return response;
        } catch (IOException e) {
            throw new SlackMessageNotSentException("Message didn't send to slack");
        }
    }

}
