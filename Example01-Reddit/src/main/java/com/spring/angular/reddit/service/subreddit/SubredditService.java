package com.spring.angular.reddit.service.subreddit;

import com.spring.angular.reddit.dto.SubredditDto;

import java.util.List;

public interface SubredditService {
    SubredditDto saveSubreddit(SubredditDto subredditDto);
    List<SubredditDto> getAllSubreddits();
    SubredditDto getSingleSubreddit(Long id);
}
