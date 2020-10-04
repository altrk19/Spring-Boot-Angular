package com.spring.angular.reddit.resource;

import com.spring.angular.reddit.model.VoteType;
import lombok.Builder;

import javax.validation.constraints.NotBlank;

@Builder
public class VoteResource extends DtoBase{
    private String identifier;

    private String postIdentifier;

    private Long createdDate;

    @NotBlank(message = "voteType cannot be blank")
    private VoteType voteType;


    public VoteResource() {
    }

    public VoteResource(String identifier, String postIdentifier, Long createdDate,
                        @NotBlank(
                                message = "voteType cannot be blank") VoteType voteType) {
        this.identifier = identifier;
        this.postIdentifier = postIdentifier;
        this.createdDate = createdDate;
        this.voteType = voteType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPostIdentifier() {
        return postIdentifier;
    }

    public void setPostIdentifier(String postIdentifier) {
        this.postIdentifier = postIdentifier;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public VoteType getVoteType() {
        return voteType;
    }

    public void setVoteType(VoteType voteType) {
        this.voteType = voteType;
    }
}