package com.spring.angular.reddit.resource;

import javax.validation.constraints.NotBlank;

public class UserRegisterResource extends DtoBase{
    @NotBlank(message = "email cannot be blank")
    private String email;

    @NotBlank(message = "username cannot be blank")
    private String username;

    @NotBlank(message = "password cannot be blank")
    private String password;

    public UserRegisterResource() {
    }

    public UserRegisterResource(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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