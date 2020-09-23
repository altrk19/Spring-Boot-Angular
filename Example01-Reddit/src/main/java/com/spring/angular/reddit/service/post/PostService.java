package com.spring.angular.reddit.service.post;

import com.spring.angular.reddit.dto.PostRequestDto;
import com.spring.angular.reddit.dto.PostResponseDto;

import java.util.List;

public interface PostService {
    void save(PostRequestDto postRequestDto);
    PostResponseDto getSinglePost(Long id);
    List<PostResponseDto> getAllPosts();
    List<PostResponseDto> getPostsBySubreddit(Long subredditId);
    List<PostResponseDto> getPostsByUsername(String username);
}
