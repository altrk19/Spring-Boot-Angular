package com.spring.angular.reddit.controller.vote;

import com.spring.angular.reddit.exception.ClientException;
import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.Vote;
import com.spring.angular.reddit.resource.VoteResource;
import com.spring.angular.reddit.service.vote.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

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

    @PostMapping("/{postIdentifier}")
    public ResponseEntity<VoteResource> addVote(@PathVariable @NotNull final String postIdentifier,
                                        @RequestBody VoteResource voteResource)
            throws ServerException, ClientException {
        log.info("Request received to add vote post with postId {}", postIdentifier);
        Vote vote = voteService.addVote(voteConverter.toEntity(voteResource, postIdentifier));
        VoteResource voteResourceAdded = voteConverter.toResource(vote);
        log.info("Request completed to add vote post with postId {}", postIdentifier);
        return ResponseEntity.status(HttpStatus.CREATED).body(voteResourceAdded);
    }

    @GetMapping("/{identifier}")
    public ResponseEntity<VoteResource> getSingleVote(@PathVariable @NotNull final String identifier)
            throws ServerException {
        log.info("Request received to get vote with id {}", identifier);
        Vote vote = voteService.getSingleVote(identifier);
        VoteResource voteResource = voteConverter.toResource(vote);
        log.info("Request completed to get vote with id {}", identifier);
        return ResponseEntity.status(HttpStatus.OK).body(voteResource);
    }

    @DeleteMapping("/{identifier}")
    public ResponseEntity<Void> deleteVote(@PathVariable @NotNull final String identifier)
            throws ServerException {
        log.info("Request received to delete vote with id {}", identifier);
        voteService.deleteSingleVote(identifier);
        log.info("Request completed to delete vote with id {}", identifier);
        return ResponseEntity.noContent().build();
    }
}