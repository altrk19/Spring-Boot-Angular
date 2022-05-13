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
import javax.validation.constraints.NotNull;

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
        return ResponseEntity.status(HttpStatus.OK).body("User created successfully");
    }

    @GetMapping("/userVerification/{token}")
    public ResponseEntity<String> verifyUser(@PathVariable @NotNull final String token) throws ServerException, ClientException {
        log.info("Request received to verify user");
        userService.verifyUser(token);
        log.info("Request completed to verify user");
        return ResponseEntity.status(HttpStatus.OK).body("User activated successfully");
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable @NotNull final String username) throws ServerException, ClientException {
        log.info("Request received to delete user");
        userService.deleteUser(username);
        log.info("Request completed to delete user");
        return ResponseEntity.noContent().build();
    }
}
