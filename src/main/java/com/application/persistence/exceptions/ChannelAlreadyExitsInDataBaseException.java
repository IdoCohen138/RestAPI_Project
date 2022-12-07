package com.application.persistence.exceptions;

public class ChannelAlreadyExitsInDataBaseException extends Exception {
    public ChannelAlreadyExitsInDataBaseException(String message) {
        super(message);
    }
}
