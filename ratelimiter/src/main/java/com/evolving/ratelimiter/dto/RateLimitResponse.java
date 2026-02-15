package com.evolving.ratelimiter.dto;

public class RateLimitResponse {
    private boolean allowed;
    private int remainingTokens;
    private long resetTimeSeconds;
    private int limit;

    public RateLimitResponse(boolean allowed, int remainingTokens, long resetTimeSeconds, int limit) {
        this.allowed = allowed;
        this.remainingTokens = remainingTokens;
        this.resetTimeSeconds = resetTimeSeconds;
        this.limit = limit;
    }
    
    public boolean isAllowed() {
        return allowed;
    }

    
    public int getRemainingTokens() {
        return remainingTokens;
    }

    public long getResetTimeSeconds() {
        return resetTimeSeconds;
    }

    public int getLimit() {
        return limit;
    }   
}
