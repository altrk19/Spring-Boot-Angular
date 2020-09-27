package com.spring.angular.reddit.controller.user;

import com.spring.angular.reddit.model.User;
import com.spring.angular.reddit.resource.UserRegisterRequestResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class UserConverter {
    private final PasswordEncoder passwordEncoder;

    public UserConverter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User toEntity(UserRegisterRequestResource userRegisterRequestResource){
        User user = new User();
        user.setUsername(userRegisterRequestResource.getUsername());
        user.setEmail(userRegisterRequestResource.getEmail());
        user.setPassword(passwordEncoder.encode(userRegisterRequestResource.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);
        return user;
    }
}
