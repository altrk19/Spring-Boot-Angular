package com.spring.angular.reddit.service.refreshtoken;

import com.spring.angular.reddit.exception.ClientException;
import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.RefreshToken;
import com.spring.angular.reddit.resource.LoginResponseResource;
import com.spring.angular.reddit.resource.LoginWithRefreshTokenResource;

public interface RefreshTokenService {

    RefreshToken generateRefreshToken(String username) throws ServerException;

    RefreshToken getRefreshTokenByToken(String token) throws ClientException;

    void deleteRefreshToken(String username) throws ServerException;

    LoginResponseResource loginWithRefreshToken(LoginWithRefreshTokenResource loginWithRefreshTokenResource)
            throws ClientException, ServerException;
}
