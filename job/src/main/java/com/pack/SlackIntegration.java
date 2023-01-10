package com.pack;

import com.pack.exceptions.SlackMessageNotSentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class SlackIntegration {
    @Qualifier("messageSender")
    @Autowired
    MessageSender messageSender;

    @Qualifier("messageRepository")
    @Autowired
    MessageRepository messageRepository;

    @Qualifier("slackRepository")
    @Autowired
    SlackRepository slackRepository;

//    @Scheduled(cron = "0 0 10 * * *")
    public void sendPeriodicMessages() {
        Specification<SlackChannel> spec = (root, query, builder) ->
                builder.equal(root.get("status"), EnumStatus.ENABLED);

        for (SlackChannel slackChannel : slackRepository.findAll(spec)) {
            try {
                String message = "You have no vulnerabilities";
                messageSender.sendMessage(slackChannel, message);
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
