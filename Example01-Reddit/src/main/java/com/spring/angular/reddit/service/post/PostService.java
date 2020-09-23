package com.spring.angular.reddit.service.post;

import com.spring.angular.reddit.dto.PostRequest;
import com.spring.angular.reddit.dto.PostResponse;

import java.util.List;

public interface PostService {
    void save(PostRequest postRequest);
    PostResponse getSinglePost(Long id);
    List<PostResponse> getAllPosts();
    List<PostResponse> getPostsBySubreddit(Long subredditId);
    List<PostResponse> getPostsByUsername(String username);
}
