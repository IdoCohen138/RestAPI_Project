package com.application.service;

import lombok.*;


import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Getter
@Setter
@EqualsAndHashCode
public class SlackChannel {

    private UUID id;
    @EqualsAndHashCode.Exclude @NotNull(message = "The request must contain webhook! (webhook)")
    private String webhook;
    @EqualsAndHashCode.Exclude @NotNull(message = "The request must contain channel name! (channelName)")
    private String channelName;
    @EqualsAndHashCode.Exclude
    private EnumStatus status = EnumStatus.ENABLED;

}
