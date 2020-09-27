package com.spring.angular.reddit.resource;

import javax.validation.constraints.NotBlank;

public class LogoutResource extends DtoBase{
    @NotBlank(message = "refreshToken cannot be blank")
    private String refreshToken;

    @NotBlank(message = "username cannot be blank")
    private String username;

    public LogoutResource() {
    }

    public LogoutResource(@NotBlank String refreshToken, String username) {
        this.refreshToken = refreshToken;
        this.username = username;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}