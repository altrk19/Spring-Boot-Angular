package com.spring.angular.reddit.service.vote;

import com.spring.angular.reddit.exception.ClientException;
import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.Vote;

public interface VoteService {
    void addVote(Vote vote) throws ServerException, ClientException;
}
