package com.spring.angular.reddit.controller.user;

import com.spring.angular.reddit.resource.LoginResponseResource;
import com.spring.angular.reddit.resource.LoginRequestResource;
import com.spring.angular.reddit.resource.RefreshTokenRequestResource;
import com.spring.angular.reddit.resource.UserRegisterRequestResource;
import com.spring.angular.reddit.exception.ClientException;
import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.service.user.UserService;
import com.spring.angular.reddit.service.refreshtoken.RefreshTokenService;
import com.spring.angular.reddit.utils.UserConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    public UserController(UserService userService,
                          RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@Valid @RequestBody UserRegisterRequestResource userRegisterRequestResource)
            throws ServerException, ClientException {
        userService.signUp(UserConverter.toUserEntity(userRegisterRequestResource));
        return ResponseEntity.status(HttpStatus.OK).body("Account Registration Successful");
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) throws ServerException, ClientException {
        userService.verifyUser(token);
        return ResponseEntity.status(HttpStatus.OK).body("Account activated successfully");

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseResource> login(@Valid @RequestBody LoginRequestResource loginRequestResource) {
        LoginResponseResource loginResponseResource = userService.login(loginRequestResource);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponseResource);
    }

    @PostMapping("/refresh/token")
    public ResponseEntity<LoginResponseResource> refreshToken(
            @Valid @RequestBody RefreshTokenRequestResource refreshTokenRequestResource) throws ClientException {
        refreshTokenService.loginWithRefreshToken(refreshTokenRequestResource);
//        return AuthenticationResponse.builder()
//                .authenticationToken(token)
//                .refreshToken(refreshTokenRequest.getRefreshToken())
//                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
//                .username(refreshTokenRequest.getUsername())
//                .build();
        LoginResponseResource loginResponseResource =
        return ResponseEntity.status(HttpStatus.OK).body(loginResponseResource);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @Valid @RequestBody RefreshTokenRequestResource refreshTokenRequestResource) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequestResource.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body("Refresh token deleted successfully");
    }
}
