package com.spring.angular.reddit.service.auth;

import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.User;
import com.spring.angular.reddit.resource.LoginRequestResource;
import com.spring.angular.reddit.resource.LoginResponseResource;
import com.spring.angular.reddit.security.JwtProvider;
import com.spring.angular.reddit.service.refreshtoken.RefreshTokenService;
import com.spring.angular.reddit.service.user.UserService;
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

        String token = jwtProvider.generateToken(principal);

        return LoginResponseResource.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken(principal.getUsername()).getToken())
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
    public void logout(String username) throws ServerException {

        refreshTokenService.deleteRefreshToken(username);
    }
}
