package com.spring.angular.reddit.resource;

import lombok.Builder;

@Builder
public class PostResponseResource extends DtoBase{
    private String identifier;
    private String postName;
    private String url;
    private String description;
    private String userName;
    private String subredditName;
    private Integer voteCount;
    private Integer commentCount;
    private Long createdDate;
    private Boolean upVote;
    private Boolean downVote;

    public PostResponseResource() {
    }

    public PostResponseResource(String identifier, String postName, String url, String description,
                                String userName, String subredditName, Integer voteCount, Integer commentCount,
                                Long createdDate, Boolean upVote, Boolean downVote) {
        this.identifier = identifier;
        this.postName = postName;
        this.url = url;
        this.description = description;
        this.userName = userName;
        this.subredditName = subredditName;
        this.voteCount = voteCount;
        this.commentCount = commentCount;
        this.createdDate = createdDate;
        this.upVote = upVote;
        this.downVote = downVote;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSubredditName() {
        return subredditName;
    }

    public void setSubredditName(String subredditName) {
        this.subredditName = subredditName;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getUpVote() {
        return upVote;
    }

    public void setUpVote(Boolean upVote) {
        this.upVote = upVote;
    }

    public Boolean getDownVote() {
        return downVote;
    }

    public void setDownVote(Boolean downVote) {
        this.downVote = downVote;
    }
}