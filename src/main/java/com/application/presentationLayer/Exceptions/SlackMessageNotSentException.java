package com.application.presentationLayer.Exceptions;


public class SlackMessageNotSentException extends Exception {
    public SlackMessageNotSentException(String message) {
        super(message);
    }
}