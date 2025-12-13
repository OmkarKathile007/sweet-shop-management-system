package com.sweetshop.backend.controller;

import com.sweetshop.backend.dto.RegisterRequest;
import com.sweetshop.backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

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
}