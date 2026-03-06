package com.example.stompdemo.model;

import java.time.Instant;

public class ChatResponse {

    private String from;
    private String content;
    private Instant timestamp;

    public ChatResponse() {
    }

    public ChatResponse(String from, String content, Instant timestamp) {
        this.from = from;
        this.content = content;
        this.timestamp = timestamp;
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

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
