package com.application.serviceLayer;

import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

@Data
@Getter @Setter @EqualsAndHashCode
public class SlackChannel {

    @NotBlank(message = "webhook is required") private String webhook;
    @EqualsAndHashCode.Exclude private String channelName;
    @EqualsAndHashCode.Exclude private EnumStatus status = EnumStatus.ENABLED;

}
