package com.spring.angular.reddit.service.post;

import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.Post;

import java.util.List;

public interface PostService {
    void save(Post post) throws ServerException;

    Post getSinglePost(Long id) throws ServerException;

    List<Post> getAllPosts();

    List<Post> getPostsBySubreddit(String subredditIdentifier) throws ServerException;

    List<Post> getPostsByUsername(String username) throws ServerException;
}
