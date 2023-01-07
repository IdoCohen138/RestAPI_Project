package com.pack;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({SlackChannel.class, LogMessages.class, LogMessagePrimaryKey.class})
public class EntityConfiguration {
}
