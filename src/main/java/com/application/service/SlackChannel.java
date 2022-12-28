package com.application.service;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "SlackChannel", schema = "public", catalog = "postgres")
public class SlackChannel implements Serializable {
    @Id
    @Type(type = "pg-uuid")
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
    @Temporal(TemporalType.DATE)
    private Date created_at;
    @EqualsAndHashCode.Exclude
    @UpdateTimestamp
    @Temporal(TemporalType.DATE)
    @Column(name = "modified_at", nullable = false, length = -1)
    private Date modified_at;


}
