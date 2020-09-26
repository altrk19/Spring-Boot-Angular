package com.spring.angular.reddit.service.refreshtoken;

import com.spring.angular.reddit.constants.RequestErrorTypes;
import com.spring.angular.reddit.resource.LoginResponseResource;
import com.spring.angular.reddit.resource.RefreshTokenRequestResource;
import com.spring.angular.reddit.exception.ClientException;
import com.spring.angular.reddit.model.RefreshToken;
import com.spring.angular.reddit.repository.RefreshTokenRepository;
import com.spring.angular.reddit.security.JwtProvider;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository,
                                   JwtProvider jwtProvider) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public LoginResponseResource loginWithRefreshToken(RefreshTokenRequestResource refreshTokenRequestResource) throws ClientException {
        validateRefreshToken(refreshTokenRequestResource.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequestResource.getUsername());
        return LoginResponseResource.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequestResource.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequestResource.getUsername())
                .build();
    }

    @Override
    @Transactional
    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    @Override
    public void validateRefreshToken(String token) throws ClientException {
        refreshTokenRepository.findByToken(token).orElseThrow(
                () -> new ClientException(RequestErrorTypes.INVALID_ACCESS_TOKEN, null, HttpStatus.FORBIDDEN));
    }
}
