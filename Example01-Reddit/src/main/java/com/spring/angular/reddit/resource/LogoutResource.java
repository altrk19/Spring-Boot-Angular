package com.spring.angular.reddit.resource;

import javax.validation.constraints.NotBlank;

public class LogoutResource extends DtoBase{
    @NotBlank(message = "username cannot be blank")
    private String username;

    public LogoutResource() {
    }

    public LogoutResource(
            @NotBlank(message = "username cannot be blank") String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}