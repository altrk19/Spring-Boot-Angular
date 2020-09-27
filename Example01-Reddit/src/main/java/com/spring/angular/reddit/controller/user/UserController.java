package com.spring.angular.reddit.controller.user;

import com.spring.angular.reddit.exception.ClientException;
import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.resource.UserRegisterResource;
import com.spring.angular.reddit.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserConverter userConverter;

    public UserController(UserService userService, UserConverter userConverter) {
        this.userService = userService;
        this.userConverter = userConverter;
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> createUser(
            @Valid @RequestBody UserRegisterResource userRegisterResource)
            throws ServerException, ClientException {
        log.info("Request received to create user with username {}", userRegisterResource.getUsername());
        userService.signUp(userConverter.toEntity(userRegisterResource));
        log.info("Request completed to create user with username {}", userRegisterResource.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body("Account Registration Successful");
    }

    @GetMapping("/userVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) throws ServerException, ClientException {
        log.info("Request received to verify user");
        userService.verifyUser(token);
        log.info("Request completed to verify user");
        return ResponseEntity.status(HttpStatus.OK).body("Account activated successfully");
    }
}
