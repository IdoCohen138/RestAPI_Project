package com.example.demo.serviceLayer;

import com.example.demo.Enum;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

@Data
@Getter @Setter @EqualsAndHashCode
public class SlackChannel {

    @NotBlank(message = "webhook is required") private String webhook;
    @EqualsAndHashCode.Exclude private String channelName;
    @EqualsAndHashCode.Exclude private Enum.status status = Enum.status.Enable;

    public void setStatus() {
        if (this.status.equals(Enum.status.Enable))
            this.status = Enum.status.Disable;
        else
            this.status = Enum.status.Enable;
    }
}
