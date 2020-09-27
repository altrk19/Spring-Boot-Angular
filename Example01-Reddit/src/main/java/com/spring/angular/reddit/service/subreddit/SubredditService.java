package com.spring.angular.reddit.service.subreddit;

import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.Subreddit;

import java.util.List;

public interface SubredditService {
    Subreddit saveSubreddit(Subreddit subreddit);

    List<Subreddit> getAllSubreddits();

    Subreddit getSingleSubreddit(Long id) throws ServerException;

    Subreddit getSingleSubredditByName(String name) throws ServerException;

    void deleteSubreddit(Long id);
}
