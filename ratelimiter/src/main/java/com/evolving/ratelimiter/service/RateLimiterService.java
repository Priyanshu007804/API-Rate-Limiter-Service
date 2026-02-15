package com.evolving.ratelimiter.service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.evolving.ratelimiter.model.TokenBucket;
@Service
public class RateLimiterService {
    private final Map<String, TokenBucket> bucketMap= new ConcurrentHashMap<>();
    
    public boolean allowRequest(String userKey) {
        TokenBucket bucket = bucketMap.computeIfAbsent(userKey, k-> new TokenBucket(10, 0.2));
        return bucket.allowRequest();
    }
    public TokenBucket getBucket(String userKey) {
         bucketMap.putIfAbsent(userKey, new TokenBucket(10, 0.2));
            return bucketMap.get(userKey);
    }
}
