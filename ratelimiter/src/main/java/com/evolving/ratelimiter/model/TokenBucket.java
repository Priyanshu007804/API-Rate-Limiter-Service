package com.evolving.ratelimiter.model;

public class TokenBucket {
    private int capacity;
    private double tokens;
    private long lastRefillTimestamp;
    private double refillRatePerSecond; 

    public TokenBucket(int capacity, double refillRatePerSecond) {
        this.capacity = capacity; //MAX CAPACITY
        this.tokens = capacity;  //CURRENT BALANCE
        this.lastRefillTimestamp = System.currentTimeMillis(); //STORES THE EXACT TIME THE BUCKET WAS LAST CHECKED
        this.refillRatePerSecond = refillRatePerSecond; 
    }

    public synchronized boolean allowRequest() {
        refillTokens();
        if (tokens >= 1) {
            tokens -= 1;
            return true;
        }
        return false;
    }

    private void refillTokens() {
        long now = System.currentTimeMillis();
        double tokensToAdd = ((now - lastRefillTimestamp) / 1000.0) * refillRatePerSecond;
        tokens = Math.min(capacity, tokens + tokensToAdd);
        lastRefillTimestamp = now;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getTokens() {
        return tokens;
    }

    public long getResetTimeSeconds(){
        if(tokens >= capacity) {
            return 0; 
        }
        double missingTokens = capacity - tokens;
        return (long) Math.ceil(missingTokens / refillRatePerSecond);
        }
    
        
        public long getNextRefillTimeMillis() {
        double missingTokens = capacity - tokens;
        if (missingTokens <= 0) {
            return 0; 
        }
        long timeToRefill = (long) Math.ceil(missingTokens / refillRatePerSecond * 1000);
        return System.currentTimeMillis() + timeToRefill;
        }
}
