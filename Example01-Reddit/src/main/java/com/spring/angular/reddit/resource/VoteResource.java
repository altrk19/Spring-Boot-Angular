package com.spring.angular.reddit.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.spring.angular.reddit.model.VoteType;

import javax.validation.constraints.NotBlank;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VoteResource {
    @NotBlank(message = "voteType cannot be blank")
    private VoteType voteType;

    @NotBlank(message = "postId cannot be blank")
    private Long postId;

    public VoteResource() {
    }

    public VoteResource(VoteType voteType, Long postId) {
        this.voteType = voteType;
        this.postId = postId;
    }

    public VoteType getVoteType() {
        return voteType;
    }

    public void setVoteType(VoteType voteType) {
        this.voteType = voteType;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}