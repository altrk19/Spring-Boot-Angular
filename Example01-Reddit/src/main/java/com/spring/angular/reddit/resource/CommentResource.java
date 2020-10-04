package com.spring.angular.reddit.resource;

import lombok.Builder;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Builder
public class CommentResource extends DtoBase {
    private String identifier;

    private String postIdentifier;

    private Long createdDate;

    @NotBlank(message = "text cannot be blank")
    private String text;

    private String userName;

    public CommentResource() {
    }

    public CommentResource(String identifier, String postIdentifier, Long createdDate,
                           @NotBlank(message = "text cannot be blank") String text, String userName) {
        this.identifier = identifier;
        this.postIdentifier = postIdentifier;
        this.createdDate = createdDate;
        this.text = text;
        this.userName = userName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPostIdentifier() {
        return postIdentifier;
    }

    public void setPostIdentifier(String postIdentifier) {
        this.postIdentifier = postIdentifier;
    }
}