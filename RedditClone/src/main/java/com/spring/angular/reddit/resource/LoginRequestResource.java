package com.spring.angular.reddit.resource;

import javax.validation.constraints.NotBlank;

public class LoginRequestResource extends DtoBase {
    @NotBlank(message = "username cannot be blank")
    private String username;

    @NotBlank(message = "password cannot be blank")
    private String password;

    public LoginRequestResource() {
    }

    public LoginRequestResource(
            @NotBlank(message = "username cannot be blank") String username,
            @NotBlank(message = "password cannot be blank") String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}