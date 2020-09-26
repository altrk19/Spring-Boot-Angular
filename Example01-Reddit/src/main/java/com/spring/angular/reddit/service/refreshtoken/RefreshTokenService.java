package com.spring.angular.reddit.service.refreshtoken;

import com.spring.angular.reddit.resource.LoginResponseResource;
import com.spring.angular.reddit.resource.RefreshTokenRequestResource;
import com.spring.angular.reddit.exception.ClientException;
import com.spring.angular.reddit.model.RefreshToken;

public interface RefreshTokenService {

    RefreshToken generateRefreshToken();

    void deleteRefreshToken(String token);

    LoginResponseResource loginWithRefreshToken(RefreshTokenRequestResource refreshTokenRequestResource) throws ClientException;

    void validateRefreshToken(String token) throws ClientException;
}
