package com.spring.angular.reddit.service.refreshtoken;

import com.spring.angular.reddit.constants.RequestErrorTypes;
import com.spring.angular.reddit.exception.ClientException;
import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.RefreshToken;
import com.spring.angular.reddit.model.User;
import com.spring.angular.reddit.repository.RefreshTokenRepository;
import com.spring.angular.reddit.resource.LoginResponseResource;
import com.spring.angular.reddit.resource.LoginWithRefreshTokenResource;
import com.spring.angular.reddit.security.JwtProvider;
import com.spring.angular.reddit.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    public RefreshTokenServiceImpl(UserService userService,
                                   RefreshTokenRepository refreshTokenRepository,
                                   JwtProvider jwtProvider) {
        this.userService = userService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public LoginResponseResource loginWithRefreshToken(LoginWithRefreshTokenResource loginWithRefreshTokenResource)
            throws ClientException, ServerException {
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
    public RefreshToken generateRefreshToken(String username) throws ServerException {
        User user = userService.getSingleUserByUsername(username);
        RefreshToken refreshToken = user.getRefreshToken();
        if (Objects.nonNull(refreshToken)) {
            return refreshToken;
        }
        refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        user.setRefreshToken(refreshToken);
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken getRefreshTokenByToken(String token) throws ClientException {
        return refreshTokenRepository.findByToken(token).orElseThrow(
                () -> new ClientException(RequestErrorTypes.INVALID_ACCESS_TOKEN, null, HttpStatus.FORBIDDEN));
    }

    @Override
    @Transactional
    public void deleteRefreshToken(String username) throws ServerException {
        User user = userService.getSingleUserByUsername(username);
        RefreshToken refreshToken = user.getRefreshToken();
        user.setRefreshToken(null);
        refreshTokenRepository.delete(refreshToken);
    }

    public void validateRefreshToken(LoginWithRefreshTokenResource loginWithRefreshTokenResource)
            throws ClientException, ServerException {
        User user = userService.getSingleUserByUsername(loginWithRefreshTokenResource.getUsername());
        RefreshToken refreshToken =
                refreshTokenRepository.findByToken(loginWithRefreshTokenResource.getRefreshToken()).orElseThrow(
                        () -> new ClientException(RequestErrorTypes.INVALID_REFRESH_TOKEN, null, HttpStatus.FORBIDDEN));

        if (!user.getRefreshToken().equals(refreshToken)) {
            throw new ClientException(RequestErrorTypes.INVALID_REFRESH_TOKEN, null, HttpStatus.FORBIDDEN);
        }
    }
}
