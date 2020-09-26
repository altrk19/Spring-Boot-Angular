package com.spring.angular.reddit.controller.post;

import com.spring.angular.reddit.resource.PostRequestResource;
import com.spring.angular.reddit.model.Post;
import com.spring.angular.reddit.model.Subreddit;

public class PostConverter {

    public static Post toPostEntity(PostRequestResource postRequestResource){
        Post post = new Post();
        post.setPostName(postRequestResource.getPostName());
        post.setUrl(postRequestResource.getUrl());
        post.setDescription(postRequestResource.getDescription());

        Subreddit subreddit = new Subreddit();
        subreddit.setName(postRequestResource.getSubredditName());
        post.setSubreddit(subreddit);

        return post;
    }
}
