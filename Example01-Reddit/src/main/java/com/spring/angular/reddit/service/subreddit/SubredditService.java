package com.spring.angular.reddit.service.subreddit;

import com.spring.angular.reddit.exception.ClientException;
import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.Subreddit;

import java.util.List;

public interface SubredditService {
    Subreddit saveSubreddit(Subreddit subreddit) throws ServerException, ClientException;

    List<Subreddit> getAllSubreddits();

    Subreddit getSingleSubreddit(String identifier) throws ServerException;

    Subreddit getSingleSubredditByName(String name) throws ServerException;

    void deleteSubreddit(String identifier) throws ServerException;
}
