package com.spring.angular.reddit.service.auth;

import com.spring.angular.reddit.constants.RequestErrorTypes;
import com.spring.angular.reddit.exception.ClientException;
import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.RefreshToken;
import com.spring.angular.reddit.model.User;
import com.spring.angular.reddit.resource.LoginRequestResource;
import com.spring.angular.reddit.resource.LoginResponseResource;
import com.spring.angular.reddit.resource.LogoutResource;
import com.spring.angular.reddit.security.JwtProvider;
import com.spring.angular.reddit.service.refreshtoken.RefreshTokenService;
import com.spring.angular.reddit.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public AuthenticationServiceImpl(
            AuthenticationManager authenticationManager, JwtProvider jwtProvider,
            RefreshTokenService refreshTokenService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
    }

    @Override
    public LoginResponseResource login(LoginRequestResource loginRequestResource) throws ServerException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestResource.getUsername(),
                        loginRequestResource.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        User user = userService.getSingleUserByUsername(principal.getUsername());

        String token = jwtProvider.generateToken(principal);

        return LoginResponseResource.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken(user).getToken())
                .expiresAt(Instant.now().toEpochMilli() + jwtProvider.getJwtExpirationInMillis())
                .username(loginRequestResource.getUsername())
                .build();
    }

    @Override
    public User getCurrentUser() throws ServerException {
        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                        getContext().getAuthentication().getPrincipal();
        return userService.getSingleUserByUsername(user.getUsername());
    }

    @Override
    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    @Override
    public User getUserByUsername(String username) throws ServerException {
        return userService.getSingleUserByUsername(username);
    }

    @Override
    public void logout(LogoutResource logoutResource) throws ClientException {
        RefreshToken refreshToken = refreshTokenService.getRefreshTokenByToken(logoutResource.getRefreshToken());

        if (!logoutResource.getUsername().equals(refreshToken.getUser().getUsername())) {
            throw new ClientException(RequestErrorTypes.INVALID_ACCESS_TOKEN, null, HttpStatus.FORBIDDEN);
        }
    }
}
