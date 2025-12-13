package com.sweetshop.backend.service;

import com.sweetshop.backend.model.User;
import com.sweetshop.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

//    public User register(String username, String password, String role) {
//        return null; // returning null to force test failure
//    }
    public User register(String username, String password, String role) {
        String encodedPassword = passwordEncoder.encode(password);
        User newUser = new User(username, encodedPassword, role);
        return userRepository.save(newUser);
    }
    public String login(String username, String password) {
        return null; 
    }
}