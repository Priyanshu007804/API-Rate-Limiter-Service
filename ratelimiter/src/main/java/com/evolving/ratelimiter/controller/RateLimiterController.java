package com.evolving.ratelimiter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.evolving.ratelimiter.service.RateLimiterService;

import com.evolving.ratelimiter.model.TokenBucket;

@RestController
@RequestMapping("/api")
public class RateLimiterController {

    private final RateLimiterService rateLimiterService;

    public RateLimiterController(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

   @GetMapping("/check")
public ResponseEntity<String> checkRateLimit(@RequestParam String user) {
    TokenBucket bucket = rateLimiterService.getBucket(user);
    boolean allowed = rateLimiterService.allowRequest(user);

    if (allowed) {
        return ResponseEntity.ok("Request Allowed ");
    } else {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Too Many Requests  (429)");
    }
}
}