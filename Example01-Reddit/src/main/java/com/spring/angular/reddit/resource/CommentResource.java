package com.spring.angular.reddit.resource;

import lombok.Builder;

import javax.validation.constraints.NotBlank;

@Builder
public class CommentResource extends DtoBase {
    private String identifier;

    private String postIdentifier;

    private Long createdDate;

    @NotBlank(message = "text cannot be blank")
    private String text;

    public CommentResource() {
    }

    public CommentResource(String identifier, String postIdentifier, Long createdDate,
                           @NotBlank(message = "text cannot be blank") String text) {
        this.identifier = identifier;
        this.postIdentifier = postIdentifier;
        this.createdDate = createdDate;
        this.text = text;
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

    public String getPostIdentifier() {
        return postIdentifier;
    }

    public void setPostIdentifier(String postIdentifier) {
        this.postIdentifier = postIdentifier;
    }
}