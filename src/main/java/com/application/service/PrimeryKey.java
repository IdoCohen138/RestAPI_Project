package com.application.service;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Embeddable
@Data
public class PrimeryKey implements Serializable {
    private UUID id;
    private String message;
    private Timestamp time;


}
