package com.application.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Data
public class LogMessagePrimaryKey implements Serializable {
    private UUID id;
    private String message;
    private Timestamp time;

}
