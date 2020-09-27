package com.spring.angular.reddit.service.auth;

import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.User;
import com.spring.angular.reddit.resource.LoginRequestResource;
import com.spring.angular.reddit.resource.LoginResponseResource;

public interface AuthenticationService {
    LoginResponseResource login(LoginRequestResource loginRequestResource);

    User getCurrentUser() throws ServerException;

    boolean isLoggedIn();

    User getUserByUsername(String username) throws ServerException;
}
