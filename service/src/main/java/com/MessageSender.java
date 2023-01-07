package com;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import exceptions.SlackMessageNotSentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
@Component("messageSender")
public class MessageSender {
    @Autowired
    Slack slack;

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
