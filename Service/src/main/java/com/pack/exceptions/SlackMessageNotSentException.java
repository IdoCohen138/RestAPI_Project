package com.pack.exceptions;


public class SlackMessageNotSentException extends Exception {
    public SlackMessageNotSentException(String message) {
        super(message);
    }
}