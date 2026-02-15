package com.evolving.ratelimiter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RateLimiterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Test 1: Health endpoint should return OK
    @Test
    public void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Service is running!"));
    }

    // Test 2: First request should be allowed
    @Test
    public void testFirstRequestAllowed() throws Exception {
        mockMvc.perform(get("/api/check").param("user", "testuser1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Request Allowed"));
    }

    // Test 3: Rate limit should block after exceeding limit
    @Test
    public void testRateLimitExceeded() throws Exception {
        String user = "testuser2";

        // First 10 requests should be allowed
        for(int i = 0; i < 10; i++){
            mockMvc.perform(get("/api/check").param("user", user))
                    .andExpect(status().isOk());
        }

        // 11th request should be blocked
        mockMvc.perform(get("/api/check").param("user", user))
                .andExpect(status().isTooManyRequests())
                .andExpect(content().string("Too Many Requests (429)"));
    }

    // Test 4: Reset should clear rate limit
    @Test
    public void testResetEndpoint() throws Exception {
        String user = "testuser3";

        // Consume tokens
        for(int i = 0; i < 10; i++){
            mockMvc.perform(get("/api/check").param("user", user))
                    .andExpect(status().isOk());
        }

        // Now blocked
        mockMvc.perform(get("/api/check").param("user", user))
                .andExpect(status().isTooManyRequests());

        // Reset
        mockMvc.perform(post("/api/reset").param("user", user))
                .andExpect(status().isOk())
                .andExpect(content().string("Rate limit reset done for user: " + user));

        // After reset, should be allowed again
        mockMvc.perform(get("/api/check").param("user", user))
                .andExpect(status().isOk())
                .andExpect(content().string("Request Allowed"));
    }

    // Test 5: Headers should be present
    @Test
    public void testHeadersPresent() throws Exception {
        mockMvc.perform(get("/api/check").param("user", "testuser4"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-RateLimit-Limit"))
                .andExpect(header().exists("X-RateLimit-Remaining"))
                .andExpect(header().exists("X-RateLimit-Reset"));
    }
}