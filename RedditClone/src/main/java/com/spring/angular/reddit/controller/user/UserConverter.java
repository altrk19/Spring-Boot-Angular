package com.spring.angular.reddit.controller.user;

import com.spring.angular.reddit.model.User;
import com.spring.angular.reddit.resource.UserRegisterResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    private final PasswordEncoder passwordEncoder;

    public UserConverter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User toEntity(UserRegisterResource userRegisterResource){
        User user = new User();
        user.setUsername(userRegisterResource.getUsername());
        user.setEmail(userRegisterResource.getEmail());
        user.setPassword(passwordEncoder.encode(userRegisterResource.getPassword()));
        user.setEnabled(false);
        return user;
    }
}
