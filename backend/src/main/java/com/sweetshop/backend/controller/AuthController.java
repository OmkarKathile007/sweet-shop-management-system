package com.sweetshop.backend.controller;

import com.sweetshop.backend.dto.RegisterRequest;
import com.sweetshop.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sweetshop.backend.dto.LoginRequest; // Add import
import com.sweetshop.backend.dto.AuthResponse;
import com.sweetshop.backend.model.User; // Import your User model
import com.sweetshop.backend.repository.UserRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    private UserRepository userRepository;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    //   test case failing
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
//        return ResponseEntity.notFound().build(); // Return 404 to force test failure
//    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // Call the service with data from the request body
        var registeredUser = authService.register(
                request.getUsername(),
                request.getPassword(),
                request.getRole()
        );
        // Return 200 OK with the created user data
        return ResponseEntity.ok(registeredUser);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // 1. Get the token (Authentication happens here)
        String token = authService.login(request.getUsername(), request.getPassword());

        // 2. Fetch the full User details from DB to get the Role
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Return Token + Username + Role
        return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getRole()));
    }
}