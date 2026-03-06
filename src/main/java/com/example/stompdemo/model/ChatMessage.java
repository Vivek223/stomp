package com.example.stompdemo.model;

import jakarta.validation.constraints.NotBlank;

public class ChatMessage {

    @NotBlank(message = "from is required")
    private String from;

    @NotBlank(message = "content is required")
    private String content;

    public ChatMessage() {
    }

    public ChatMessage(String from, String content) {
        this.from = from;
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
