package com.application.job;

import com.application.service.*;
import com.application.service.exceptions.SlackMessageNotSentException;
import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;

@Service
public class SlackIntegration {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    SlackRepository slackRepository;
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

    @Scheduled(cron = "0 0 10 * * *")
    public void sendPeriodicMessages() {
        Specification<SlackChannel> spec = (root, query, builder) ->
                builder.equal(root.get("status"), EnumStatus.ENABLED);

        for (SlackChannel slackChannel : slackRepository.findAll(spec)) {
            try {
                String message = "You have no vulnerabilities";
                sendMessage(slackChannel, message);
                addLogMessage(message, slackChannel);
            } catch (SlackMessageNotSentException slackMessageNotSentException) {
                System.out.println(slackMessageNotSentException.getMessage());
            }
        }
    }

    private void addLogMessage(String message, SlackChannel slackChannel) {
        LogMessages logMessages;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        LogMessagePrimaryKey logMessagePrimaryKey = new LogMessagePrimaryKey(slackChannel.getId(), message, timestamp);
        logMessages = new LogMessages(slackChannel, logMessagePrimaryKey, slackChannel.getId(), message, timestamp);
        messageRepository.save(logMessages);
    }

}
