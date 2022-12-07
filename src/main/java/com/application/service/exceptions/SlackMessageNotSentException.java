package com.application.service.exceptions;


public class SlackMessageNotSentException extends Exception {
    public SlackMessageNotSentException(String message) {
        super(message);
    }
}