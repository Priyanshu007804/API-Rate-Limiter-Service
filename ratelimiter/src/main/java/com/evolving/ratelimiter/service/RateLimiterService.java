package com.evolving.ratelimiter.service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.evolving.ratelimiter.dto.RateLimitResponse;
import com.evolving.ratelimiter.model.TokenBucket;
@Service
public class RateLimiterService {
    private final Map<String, TokenBucket> bucketMap= new ConcurrentHashMap<>();
    
    public RateLimitResponse checkRateLimit(String userKey) {
        bucketMap.putIfAbsent(userKey, new TokenBucket(10, 0.2));
        TokenBucket bucket = bucketMap.get(userKey);
        boolean allowed = bucket.allowRequest();
        int limit = bucket.getCapacity();
        int remainingTokens = (int) bucket.getTokens();
        long resetTime = bucket.getNextRefillTimeMillis();
        return new RateLimitResponse(allowed, remainingTokens, resetTime, limit);
    }
    public TokenBucket getBucket(String userKey) {
         bucketMap.putIfAbsent(userKey, new TokenBucket(10, 0.2));
            return bucketMap.get(userKey);
    }
    public void resetBucket(String userKey) {
        bucketMap.put(userKey, new TokenBucket(10, 0.2));
    }
    public void resetUser(String userKey) {
        bucketMap.remove(userKey);
    }
}
