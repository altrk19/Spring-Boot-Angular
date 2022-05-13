package com.spring.angular.reddit.controller.vote;

import com.spring.angular.reddit.model.Post;
import com.spring.angular.reddit.model.Vote;
import com.spring.angular.reddit.resource.VoteResource;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class VoteConverter {
    public Vote toEntity(VoteResource voteResource, String postIdentifier) {
        Vote vote = new Vote();
        vote.setVoteType(voteResource.getVoteType());

        Post post = new Post();
        post.setIdentifier(postIdentifier);
        vote.setPost(post);

        return vote;
    }

    public VoteResource toResource(Vote vote) {
        return VoteResource.builder()
                .identifier(vote.getIdentifier())
                .postIdentifier(vote.getPost().getIdentifier())
                .createdDate(vote.getCreatedDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .voteType(vote.getVoteType())
                .build();
    }

}
