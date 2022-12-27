//package com.application.persistence.Entities;
//
//import com.application.service.EnumStatus;
//import org.hibernate.annotations.Type;
//
//import javax.persistence.*;
//import java.util.UUID;
//
//@Entity
//@Table(name = "SlackChannel", schema = "public", catalog = "postgres")
//public class SlackChannelEntity {
//    @Basic
//    @Column(name = "webhook", nullable = false, length = -1)
//    private String webhook;
//    @Basic
//    @Column(name = "channelname", nullable = false, length = -1)
//    private String channelname;
//    @Id
//    @Type(type = "pg-uuid")
//    @Column(name = "id", nullable = false,columnDefinition = "uuid")
//    private UUID id;
//    @Basic
//    @Type(type = "com.application.service.EnumStatus")
//    @Column(name = "status", length = -1)
//    private EnumStatus status;
//
//    public String getWebhook() {
//        return webhook;
//    }
//
//    public void setWebhook(String webhook) {
//        this.webhook = webhook;
//    }
//
//    public String getChannelname() {
//        return channelname;
//    }
//
//    public void setChannelname(String channelname) {
//        this.channelname = channelname;
//    }
//
//    public UUID getId() {
//        return id;
//    }
//
//    public void setId(UUID id) {
//        this.id = id;
//    }
//
//    public EnumStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(EnumStatus status) {
//        this.status = status;
//    }
//}
