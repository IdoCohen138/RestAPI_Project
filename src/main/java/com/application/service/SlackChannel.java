package com.application.service;

//import com.slack.api.model.block.element.RichTextSectionElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.Type;
//import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "slackchannel", schema = "public", catalog = "postgres")
public class SlackChannel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "uuid", unique = true)
    private UUID id;
    @EqualsAndHashCode.Exclude
    @NotNull(message = "The request must contain webhook! (webhook)")
    @Basic
    @Column(name = "webhook", nullable = false, length = -1, unique = true)
    private String webhook;
    @Basic
    @Column(name = "channelname", nullable = false, length = -1)
    @EqualsAndHashCode.Exclude
    @NotNull(message = "The request must contain channel name! (channelName)")
    private String channelName;
    @EqualsAndHashCode.Exclude
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = -1)
    private EnumStatus status;

    @EqualsAndHashCode.Exclude
    @Column(name = "created_at", nullable = false, length = -1)
    @CreationTimestamp
    private Timestamp  created_at;
    @EqualsAndHashCode.Exclude
    @UpdateTimestamp
    @Column(name = "modified_at", nullable = false, length = -1)
    private Timestamp modified_at;


}
