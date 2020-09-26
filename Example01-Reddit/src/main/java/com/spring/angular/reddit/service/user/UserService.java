package com.spring.angular.reddit.service.user;

import com.spring.angular.reddit.resource.LoginResponseResource;
import com.spring.angular.reddit.resource.LoginRequestResource;
import com.spring.angular.reddit.exception.ClientException;
import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.User;

public interface UserService {
    void signUp(User user) throws ServerException, ClientException;

    void verifyUser(String token) throws ServerException, ClientException;

    LoginResponseResource login(LoginRequestResource loginRequestResource);

    User getCurrentUser() throws ServerException;

    boolean isLoggedIn();

    User getUserByUsername(String username) throws ServerException;
}