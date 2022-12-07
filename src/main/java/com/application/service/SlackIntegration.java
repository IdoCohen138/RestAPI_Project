package com.application.service;

import com.application.persistence.Exceptions.SlackMessageNotSentException;
import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;

import java.io.IOException;

public class SlackIntegration {
    Slack slack = Slack.getInstance();
    String webhookUrl;

    public WebhookResponse sendMessage(SlackChannel slackChannel, String message) throws SlackMessageNotSentException {
        Payload payload = Payload.builder().text(message).build();
        this.webhookUrl = slackChannel.getWebhook();
        try {
            WebhookResponse response = slack.send(webhookUrl, payload);
            System.out.println(response); // WebhookResponse(code=200, message=OK, body=ok)
            return response;
        } catch (IOException e) {
            throw new SlackMessageNotSentException("Message didn't send to slack");
        }
    }

}
