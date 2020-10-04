package com.spring.angular.reddit.resource;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class SubredditResource extends DtoBase{
    private Long id;

    @NotBlank(message = "name cannot be blank")
    private String name;

    @NotBlank(message = "description cannot be blank")
    private String description;

    private Integer numberOfPosts;

    private Long createdDate;

    public SubredditResource() {
    }

    public SubredditResource(Long id,
                             @NotBlank(message = "name cannot be blank") String name,
                             @NotBlank(
                                     message = "description cannot be blank") String description,
                             Integer numberOfPosts, Long createdDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.numberOfPosts = numberOfPosts;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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