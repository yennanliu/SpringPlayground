package com.yen.RedisCache1.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Cacheable(value = "users", key = "#id")
    public String getUserById(String id) {
        // Simulate a time-consuming database operation
        simulateDelay();
        System.out.println(">>> Query DB get user info");
        return "User" + id;
    }

    private void simulateDelay() {
        try {
            Thread.sleep(3000); // 3 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}