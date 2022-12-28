package com.application.service;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Data @Setter @Getter
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "SlackChannel", schema = "public", catalog = "postgres")
public class SlackChannel implements Serializable {
    @Id
    @Type(type = "pg-uuid")
    @Column(name = "id", nullable = false,columnDefinition = "uuid" ,unique = true)
    private UUID id;
    @EqualsAndHashCode.Exclude @NotNull(message = "The request must contain webhook! (webhook)")
    @Basic
    @Column(name = "webhook", nullable = false, length = -1,unique = true)
    private String webhook;
    @Basic
    @Column(name = "channelname", nullable = false, length = -1)
    @EqualsAndHashCode.Exclude @NotNull(message = "The request must contain channel name! (channelName)")
    private String channelName;
    @EqualsAndHashCode.Exclude
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = -1)
    private EnumStatus status;
    @EqualsAndHashCode.Exclude
    private Date created_at;
    @EqualsAndHashCode.Exclude
    private Date modified_at;





}
