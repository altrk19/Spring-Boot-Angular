package com.spring.angular.reddit.service.refreshtoken;

import com.spring.angular.reddit.model.RefreshToken;

public interface RefreshTokenService {
    RefreshToken generateRefreshToken();
    void deleteRefreshToken(String token);
    void validateRefreshToken(String token);
}
