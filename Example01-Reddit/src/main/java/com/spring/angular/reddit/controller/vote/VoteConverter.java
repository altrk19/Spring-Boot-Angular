package com.spring.angular.reddit.controller.vote;

import com.spring.angular.reddit.model.Post;
import com.spring.angular.reddit.model.Vote;
import com.spring.angular.reddit.resource.VoteResource;
import org.springframework.stereotype.Component;

@Component
public class VoteConverter {
    public Vote toEntity(VoteResource voteResource){
        Vote vote = new Vote();
        vote.setVoteType(voteResource.getVoteType());

        Post post = new Post();
        post.setPostId(voteResource.getPostId());
        vote.setPost(post);

        return vote;
    }
}
