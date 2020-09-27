package com.spring.angular.reddit.service.refreshtoken;

import com.spring.angular.reddit.constants.RequestErrorTypes;
import com.spring.angular.reddit.exception.ClientException;
import com.spring.angular.reddit.model.RefreshToken;
import com.spring.angular.reddit.model.User;
import com.spring.angular.reddit.repository.RefreshTokenRepository;
import com.spring.angular.reddit.resource.LoginResponseResource;
import com.spring.angular.reddit.resource.LoginWithRefreshTokenResource;
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
    public LoginResponseResource loginWithRefreshToken(LoginWithRefreshTokenResource loginWithRefreshTokenResource)
            throws ClientException {
        validateRefreshToken(loginWithRefreshTokenResource);
        String token = jwtProvider.generateTokenWithUserName(loginWithRefreshTokenResource.getUsername());
        return LoginResponseResource.builder()
                .authenticationToken(token)
                .refreshToken(loginWithRefreshTokenResource.getRefreshToken())
                .expiresAt(Instant.now().toEpochMilli() + jwtProvider.getJwtExpirationInMillis())
                .username(loginWithRefreshTokenResource.getUsername())
                .build();
    }

    @Override
    @Transactional
    public RefreshToken generateRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken getRefreshTokenByToken(String token) throws ClientException {
        return refreshTokenRepository.findByToken(token).orElseThrow(
                () -> new ClientException(RequestErrorTypes.INVALID_ACCESS_TOKEN, null, HttpStatus.FORBIDDEN));
    }

    @Override
    @Transactional
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    public void validateRefreshToken(LoginWithRefreshTokenResource loginWithRefreshTokenResource)
            throws ClientException {
        RefreshToken refreshToken =
                refreshTokenRepository.findByToken(loginWithRefreshTokenResource.getRefreshToken()).orElseThrow(
                        () -> new ClientException(RequestErrorTypes.INVALID_ACCESS_TOKEN, null, HttpStatus.FORBIDDEN));

        if (!loginWithRefreshTokenResource.getUsername().equals(refreshToken.getUser().getUsername())) {
            throw new ClientException(RequestErrorTypes.INVALID_ACCESS_TOKEN, null, HttpStatus.FORBIDDEN);
        }
    }
}
