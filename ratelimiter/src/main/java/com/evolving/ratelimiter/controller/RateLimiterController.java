package com.evolving.ratelimiter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.evolving.ratelimiter.dto.RateLimitResponse;
import com.evolving.ratelimiter.service.RateLimiterService;

@RestController
@RequestMapping("/api")
public class RateLimiterController {

    private final RateLimiterService rateLimiterService;

    public RateLimiterController(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @GetMapping("/check")
    public ResponseEntity<String> checkRateLimit(@RequestParam String user) {
        RateLimitResponse response = rateLimiterService.checkRateLimit(user);
        return ResponseEntity.status(response.isAllowed() ? HttpStatus.OK : HttpStatus.TOO_MANY_REQUESTS)
                .header("X-RateLimit-Limit", String.valueOf(response.getLimit()))
                .header("X-RateLimit-Remaining", String.valueOf(response.getRemainingTokens()))
                .header("X-RateLimit-Reset", String.valueOf(response.getResetTimeSeconds()))
                .body(response.isAllowed() ? "Request Allowed" : "Too Many Requests (429)");
    }

    @PostMapping("/reset")
    public ResponseEntity<String> reset(@RequestParam String user) {
        rateLimiterService.resetUser(user);
        return ResponseEntity.ok("Rate limit reset done for user: " + user);
    }
}