package com.application.persistence.Exceptions;


public class SlackMessageNotSentException extends Exception {
    public SlackMessageNotSentException(String message) {
        super(message);
    }
}