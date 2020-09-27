package com.spring.angular.reddit.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.validation.constraints.NotBlank;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostRequestResource {
    private Long postId;

    @NotBlank(message = "subredditName cannot be blank")
    private String subredditName;

    @NotBlank(message = "postName cannot be blank")
    private String postName;

    @NotBlank(message = "url; cannot be blank")
    private String url;

    @NotBlank(message = "description cannot be blank")
    private String description;

    public PostRequestResource() {
    }

    public PostRequestResource(Long postId, String subredditName, String postName, String url, String description) {
        this.postId = postId;
        this.subredditName = subredditName;
        this.postName = postName;
        this.url = url;
        this.description = description;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getSubredditName() {
        return subredditName;
    }

    public void setSubredditName(String subredditName) {
        this.subredditName = subredditName;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}