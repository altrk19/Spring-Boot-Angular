package com.spring.angular.reddit.controller.vote;

import com.spring.angular.reddit.exception.ClientException;
import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.Vote;
import com.spring.angular.reddit.resource.VoteResource;
import com.spring.angular.reddit.service.vote.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votes")
@Slf4j
public class VoteController {
    private final VoteService voteService;
    private final VoteConverter voteConverter;

    public VoteController(VoteService voteService, VoteConverter voteConverter) {
        this.voteService = voteService;
        this.voteConverter = voteConverter;
    }

    @PostMapping
    public ResponseEntity<Void> addVote(@RequestBody VoteResource voteResource)
            throws ServerException, ClientException {
        log.info("Request received to add vote post with postId {}", voteResource.getPostId());
        Vote vote = voteConverter.toEntity(voteResource);
        voteService.addVote(vote);
        log.info("Request completed to add vote post with postId {}", voteResource.getPostId());
        return ResponseEntity.ok().build();
    }
}