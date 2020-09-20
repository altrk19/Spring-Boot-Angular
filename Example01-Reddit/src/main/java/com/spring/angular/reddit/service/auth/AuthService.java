package com.spring.angular.reddit.service.auth;

import com.spring.angular.reddit.dto.AuthenticationResponse;
import com.spring.angular.reddit.dto.LoginRequest;
import com.spring.angular.reddit.dto.RegisterRequest;

public interface AuthService {
    void signUp(RegisterRequest registerRequest);

    void verifyAccount(String token);

    AuthenticationResponse login(LoginRequest loginRequest);
}