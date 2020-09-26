package com.spring.angular.reddit.utils;

import com.spring.angular.reddit.resource.UserRegisterRequestResource;
import com.spring.angular.reddit.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;

@Component
public class UserConverter {
    private static PasswordEncoder passwordEncoder;
    private final PasswordEncoder passwordEncoderAutowired;

    public UserConverter(PasswordEncoder passwordEncoderAutowired) {
        this.passwordEncoderAutowired = passwordEncoderAutowired;
    }

    @PostConstruct
    private void init() {
        passwordEncoder = passwordEncoderAutowired;
    }

    public static User toUserEntity(UserRegisterRequestResource userRegisterRequestResource){
        User user = new User();
        user.setUsername(userRegisterRequestResource.getUsername());
        user.setEmail(userRegisterRequestResource.getEmail());
        user.setPassword(passwordEncoder.encode(userRegisterRequestResource.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);
        return user;
    }
}
