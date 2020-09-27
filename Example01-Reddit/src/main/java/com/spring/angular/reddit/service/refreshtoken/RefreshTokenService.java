package com.spring.angular.reddit.service.refreshtoken;

import com.spring.angular.reddit.model.User;
import com.spring.angular.reddit.resource.LoginResponseResource;
import com.spring.angular.reddit.resource.LoginWithRefreshTokenResource;
import com.spring.angular.reddit.exception.ClientException;
import com.spring.angular.reddit.model.RefreshToken;

public interface RefreshTokenService {

    RefreshToken generateRefreshToken(User user);

    RefreshToken getRefreshTokenByToken(String token) throws ClientException;

    void deleteRefreshToken(String token);

    LoginResponseResource loginWithRefreshToken(LoginWithRefreshTokenResource loginWithRefreshTokenResource)
            throws ClientException;
}
