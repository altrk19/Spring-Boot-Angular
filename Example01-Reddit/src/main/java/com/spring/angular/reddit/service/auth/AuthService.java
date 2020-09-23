package com.spring.angular.reddit.service.auth;

import com.spring.angular.reddit.dto.AuthenticationResponse;
import com.spring.angular.reddit.dto.LoginRequest;
import com.spring.angular.reddit.dto.RegisterRequest;
import com.spring.angular.reddit.model.User;

public interface AuthService {
    void signUp(RegisterRequest registerRequest);

    void verifyAccount(String token);

    AuthenticationResponse login(LoginRequest loginRequest);

    User getCurrentUser();

    boolean isLoggedIn();
}