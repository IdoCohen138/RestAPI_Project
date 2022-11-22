package com.example.demo.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageInterceptorController {
    private final MessageSender messageSender;

    public MessageInterceptorController(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
    @PostMapping(path = "/messages/{userName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendMessage(@PathVariable String userName, @RequestBody Message message) throws JsonProcessingException {
        try{
            messageSender.sendMessage(userName, message);
            return ResponseEntity.ok().build();
        }
        catch (JsonProcessingException e){
            return ResponseEntity.badRequest().build();
        }
    }
}
