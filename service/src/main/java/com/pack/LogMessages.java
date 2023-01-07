package com.pack;



import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
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
    private LogMessagePrimaryKey id;

    @Column(name = "id", nullable = false, columnDefinition = "uuid", insertable = false, updatable = false)
    private UUID uuid;

    @Basic
    @Column(name = "message", nullable = false, length = -1, insertable = false, updatable = false)
    private String message;

    @Column(name = "time", nullable = false, length = -1, insertable = false, updatable = false)
    @CreationTimestamp
    private Timestamp time;


}
