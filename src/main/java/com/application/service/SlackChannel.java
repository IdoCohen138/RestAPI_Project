package com.application.service;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.EnumMap;
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
//    @Type(type = "com.application.service.EnumStatus")
    @Column(name = "status", length = -1)
//    @Transient
    private String status;




}
