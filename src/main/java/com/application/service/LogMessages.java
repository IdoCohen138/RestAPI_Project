package com.application.service;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity
@Table(name = "logmessages", schema = "public", catalog = "postgres")
public class LogMessages implements Serializable {

    @ManyToOne
    @JoinColumn(name = "logmessages_slackchannel")
    @JsonIgnore
    private SlackChannel slackChannel;

    @EmbeddedId
    @Column(name = "logmessages_slackchannel")
    private PrimeryKey id;

    @Column(name = "id", nullable = false, columnDefinition = "uuid", insertable = false, updatable = false)
    private UUID uuid;

    @Basic
    @Column(name = "message", nullable = false, length = -1, insertable = false, updatable = false)
    private String message;

    @Column(name = "time", nullable = false, length = -1, insertable = false, updatable = false)
    @CreationTimestamp
    private Timestamp time;

    public LogMessages(UUID uuid, String message, Timestamp timestamp, SlackChannel slackChannel) {
        this.id = new PrimeryKey();
        this.id.setId(uuid);
        this.id.setMessage(message);
        this.id.setTime(timestamp);
        this.uuid = uuid;
        this.message = message;
        this.time = timestamp;
        this.slackChannel = slackChannel;
    }

    public LogMessages() {
    }

}
