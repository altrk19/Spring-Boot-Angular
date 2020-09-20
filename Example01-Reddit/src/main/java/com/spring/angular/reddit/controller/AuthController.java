package com.spring.angular.reddit.controller;

import com.spring.angular.reddit.dto.AuthenticationResponse;
import com.spring.angular.reddit.dto.LoginRequest;
import com.spring.angular.reddit.dto.RegisterRequest;
import com.spring.angular.reddit.service.auth.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody RegisterRequest registerRequest) {
        authService.signUp(registerRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Account Registration Successful");
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return ResponseEntity.status(HttpStatus.OK).body("Account activated successfully");

    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        AuthenticationResponse authenticationResponse = authService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse);

    }
}
