package com.sweetshop.backend.service;

import org.springframework.stereotype.Service;

@Service
public class JwtService {
    // Secret key for signing tokens (In production, this should be in properties!)
    private static final String SECRET_KEY = "supersecretkeythatisverylongandsecureforhmacsha256";

    public String generateToken(String username) {
        return null; // Force test failure
    }
}