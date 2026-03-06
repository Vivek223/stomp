package com.example.stompdemo.controller;

import com.example.stompdemo.model.ChatMessage;
import com.example.stompdemo.model.ChatResponse;
import jakarta.validation.Valid;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @PostMapping
    public ChatResponse submitMessage(@Valid @RequestBody ChatMessage message) {
        return toResponse(message);
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatResponse sendMessage(@Valid @Payload ChatMessage message) {
        return toResponse(message);
    }

    private ChatResponse toResponse(ChatMessage message) {
        return new ChatResponse(message.getFrom(), message.getContent(), Instant.now());
    }
}
