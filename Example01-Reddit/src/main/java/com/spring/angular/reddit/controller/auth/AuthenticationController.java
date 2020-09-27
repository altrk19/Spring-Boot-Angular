package com.spring.angular.reddit.controller.auth;

import com.spring.angular.reddit.exception.ClientException;
import com.spring.angular.reddit.resource.LoginRequestResource;
import com.spring.angular.reddit.resource.LoginResponseResource;
import com.spring.angular.reddit.resource.RefreshTokenRequestResource;
import com.spring.angular.reddit.service.auth.AuthenticationService;
import com.spring.angular.reddit.service.refreshtoken.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthenticationController {
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(RefreshTokenService refreshTokenService,
                                    AuthenticationService authenticationService) {
        this.refreshTokenService = refreshTokenService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseResource> login(@Valid @RequestBody LoginRequestResource loginRequestResource) {
        log.info("Request received to login user with username {}", loginRequestResource.getUsername());
        LoginResponseResource loginResponseResource = authenticationService.login(loginRequestResource);
        log.info("Request completed to login user with username {}", loginRequestResource.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(loginResponseResource);
    }

    @PostMapping("/refresh/token")
    public ResponseEntity<LoginResponseResource> loginWithRefreshToken(
            @Valid @RequestBody RefreshTokenRequestResource refreshTokenRequestResource) throws ClientException {
        log.info("Request received to login with refresh token with username {}",
                refreshTokenRequestResource.getUsername());
        LoginResponseResource loginResponseResource =
                refreshTokenService.loginWithRefreshToken(refreshTokenRequestResource);
        log.info("Request completed to login with refresh token with username {}",
                refreshTokenRequestResource.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(loginResponseResource);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @Valid @RequestBody RefreshTokenRequestResource refreshTokenRequestResource) {
        log.info("Request received to logout with with username {}", refreshTokenRequestResource.getUsername());
        refreshTokenService.deleteRefreshToken(refreshTokenRequestResource.getRefreshToken());
        log.info("Request completed to logout with with username {}", refreshTokenRequestResource.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body("Refresh token deleted successfully");
    }
}
