package com.example.stompdemo.model;

import java.time.Instant;

public class HealthResponse {

    private String status;
    private Instant timestamp;

    public HealthResponse(String status, Instant timestamp) {
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
