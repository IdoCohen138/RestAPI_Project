package com.example.demo.serviceLayer;
import com.example.demo.Enum;
import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import java.io.IOException;
import java.time.LocalTime;

public class SlackIntegration {
   //private LocalTime lt;
   Slack slack = Slack.getInstance();
   String webhookUrl;
   Payload payload;
    public SlackIntegration(String message){
        //this.lt = LocalTime.now();
        this.payload = Payload.builder().text(message).build();
    }

    public void sendMessage(SlackChannel sc) throws IOException {
        this.webhookUrl = sc.getWebhook();
            if (sc.getStatus().equals(Enum.status.Enable)) {
                WebhookResponse response = slack.send(webhookUrl, payload);
                System.out.println(response); // WebhookResponse(code=200, message=OK, body=ok)
            }
    }

}
