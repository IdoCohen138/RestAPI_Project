package com.application.serviceLayer;
import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import java.io.IOException;
import java.time.LocalTime;

public class SlackIntegration {
   private LocalTime lt;

    public SlackIntegration(){
        this.lt = LocalTime.now();
    }

    public void sendPeriodicMessage(SlackChannel sc) throws IOException {
        Slack slack = Slack.getInstance();
        String webhookUrl = sc.getWebhook();
        Payload payload = Payload.builder().text("You have no vulnerabilities").build();
        while (sc.getStatus().equals(EnumStatus.ENABLED)) {
            if (this.lt.getHour() == 10) {
                WebhookResponse response = slack.send(webhookUrl, payload);
                System.out.println(response); // WebhookResponse(code=200, message=OK, body=ok)
            }
        }
    }

    public void sendStatusMessage(SlackChannel sc) throws IOException {
        Slack slack = Slack.getInstance();
        String webhookUrl = sc.getWebhook();
        Payload payload = Payload.builder().text("Channel status changed").build();
        WebhookResponse response = slack.send(webhookUrl, payload);
        System.out.println(response); // WebhookResponse(code=200, message=OK, body=ok)
    }
}
