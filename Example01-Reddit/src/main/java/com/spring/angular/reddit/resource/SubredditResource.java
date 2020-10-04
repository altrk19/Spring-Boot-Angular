package com.spring.angular.reddit.resource;

import javax.validation.constraints.NotBlank;

public class SubredditResource extends DtoBase{

    private String identifier;

    @NotBlank(message = "name cannot be blank")
    private String name;

    @NotBlank(message = "description cannot be blank")
    private String description;

    private Integer numberOfPosts;

    private Long createdDate;

    public SubredditResource() {
    }

    public SubredditResource(String identifier,
                             @NotBlank(message = "name cannot be blank") String name,
                             @NotBlank(
                                     message = "description cannot be blank") String description,
                             Integer numberOfPosts, Long createdDate) {
        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.numberOfPosts = numberOfPosts;
        this.createdDate = createdDate;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumberOfPosts() {
        return numberOfPosts;
    }

    public void setNumberOfPosts(Integer numberOfPosts) {
        this.numberOfPosts = numberOfPosts;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }
}