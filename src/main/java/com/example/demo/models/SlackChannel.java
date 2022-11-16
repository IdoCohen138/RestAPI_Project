package com.example.demo.models;

import com.example.demo.Enum;

public class SlackChannel {
    private String webhook;
    private String channelName;
    private Enum.status status = Enum.status.Enable;

    public SlackChannel(String webhook, String channelName) {
        this.webhook = webhook;
        this.channelName = channelName;
    }

    public void setStatus() {
        if (this.status.equals(Enum.status.Enable))
            this.status = Enum.status.Disable;
        else
            this.status = Enum.status.Enable;
    }

    public Enum.status getStatus() {
        return status;
    }

    public String getWebhook() {
        return webhook;
    }

    public String getChannelName() {
        return channelName;
    }
}
