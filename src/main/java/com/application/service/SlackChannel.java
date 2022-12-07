package com.application.service;

import lombok.*;


import java.util.UUID;

@Data
@Getter
@Setter
@EqualsAndHashCode
public class SlackChannel {

    private UUID id;
    @EqualsAndHashCode.Exclude
    private String webhook;
    @EqualsAndHashCode.Exclude
    private String channelName;
    @EqualsAndHashCode.Exclude
    private EnumStatus status = EnumStatus.ENABLED;

}
