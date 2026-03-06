package com.example.stompdemo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void submitMessageShouldReturnTimestampedResponse() throws Exception {
        String payload = """
                {
                  \"from\": \"alice\",
                  \"content\": \"hello\"
                }
                """;

        mockMvc.perform(post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.from").value("alice"))
                .andExpect(jsonPath("$.content").value("hello"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void submitMessageShouldFailValidation() throws Exception {
        String payload = """
                {
                  \"from\": \"\",
                  \"content\": \"\"
                }
                """;

        mockMvc.perform(post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }
}
