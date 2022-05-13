package com.spring.angular.reddit.service.vote;

import com.spring.angular.reddit.exception.ClientException;
import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.Post;
import com.spring.angular.reddit.model.User;
import com.spring.angular.reddit.model.Vote;
import com.spring.angular.reddit.model.VoteType;

public interface VoteService {
    Vote addVote(Vote vote) throws ServerException, ClientException;

    Vote getSingleVote(String identifier) throws ServerException;

    void deleteSingleVote(String identifier) throws ServerException;

    Vote getVoteForPost(Post post, User currentUser);

    VoteType getVoteType(Post post) throws ServerException;

}
