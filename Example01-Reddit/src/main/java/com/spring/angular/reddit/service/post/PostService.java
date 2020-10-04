package com.spring.angular.reddit.service.post;

import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.Post;

import java.util.List;

public interface PostService {
    Post savePost(Post post) throws ServerException;

    Post getSinglePost(String identifier) throws ServerException;

    List<Post> getAllPosts();

    List<Post> getAllPostsBySubredditIdentifier(String subredditIdentifier) throws ServerException;

    List<Post> getAllPostsByUsername(String username) throws ServerException;

    void deleteSinglePost(String identifier) throws ServerException;
}
