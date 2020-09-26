package com.spring.angular.reddit.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Builder
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentResource extends DtoBase {
    private Long id;

    @NotBlank(message = "postId cannot be blank")
    private Long postId;

    private Instant createdDate;

    @NotBlank(message = "text cannot be blank")
    private String text;

    private String userName;

    public CommentResource() {
    }

    public CommentResource(Long id, Long postId, Instant createdDate, String text, String userName) {
        this.id = id;
        this.postId = postId;
        this.createdDate = createdDate;
        this.text = text;
        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
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
}