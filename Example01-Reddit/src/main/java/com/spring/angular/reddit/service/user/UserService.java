package com.spring.angular.reddit.service.user;

import com.spring.angular.reddit.exception.ClientException;
import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.User;

public interface UserService {
    void signUp(User user) throws ServerException, ClientException;

    void verifyUser(String token) throws ServerException, ClientException;

    User getSingleUserByUsername(String username) throws ServerException;
}