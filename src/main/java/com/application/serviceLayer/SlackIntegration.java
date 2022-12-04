package com.application.serviceLayer;
import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import java.io.IOException;
import java.time.LocalTime;

public class SlackIntegration {
   Slack slack= Slack.getInstance();
   String webhookUrl;

    public WebhookResponse sendMessage(SlackChannel slackChannel,String message) throws IOException {
        Payload payload = Payload.builder().text(message).build();
        this.webhookUrl = slackChannel.getWebhook();
        WebhookResponse response = slack.send(webhookUrl, payload);
        System.out.println(response); // WebhookResponse(code=200, message=OK, body=ok)
        return response;
    }

}
