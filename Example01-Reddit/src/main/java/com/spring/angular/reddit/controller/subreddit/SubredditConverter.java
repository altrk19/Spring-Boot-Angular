package com.spring.angular.reddit.controller.subreddit;

import com.spring.angular.reddit.model.Subreddit;
import com.spring.angular.reddit.resource.SubredditResource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class SubredditConverter {
    public Subreddit toEntity(SubredditResource subredditResource) {
        Subreddit subreddit = new Subreddit();
        subreddit.setName(subredditResource.getName());
        subreddit.setDescription(subredditResource.getDescription());
        return subreddit;
    }

    public SubredditResource toResource(Subreddit subreddit) {
        return SubredditResource.builder()
                .id(subreddit.getId())
                .name(subreddit.getName())
                .description(subreddit.getDescription())
                .numberOfPosts(subreddit.getPosts().size())
                .build();
    }

    public List<SubredditResource> toResourceList(List<Subreddit> subreddits) {
        return subreddits.stream().filter(Objects::nonNull).map(this::toResource)
                .collect(Collectors.toList());
    }

}