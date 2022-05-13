package com.spring.angular.reddit.controller.subreddit;

import com.spring.angular.reddit.exception.ClientException;
import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.Subreddit;
import com.spring.angular.reddit.resource.SubredditResource;
import com.spring.angular.reddit.service.subreddit.SubredditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@Slf4j
public class SubredditController {
    private final SubredditService subredditService;
    private final SubredditConverter subredditConverter;

    public SubredditController(SubredditService subredditService,
                               SubredditConverter subredditConverter) {
        this.subredditService = subredditService;
        this.subredditConverter = subredditConverter;
    }

    @GetMapping
    public ResponseEntity<List<SubredditResource>> getAllSubreddits() {
        log.info("Request received to gell all subreddits");
        List<Subreddit> subreddits = subredditService.getAllSubreddits();
        List<SubredditResource> subredditResources = subredditConverter.toResourceList(subreddits);
        log.info("Request received to gell all subreddits");
        return ResponseEntity.status(HttpStatus.OK).body(subredditResources);
    }

    @PostMapping
    public ResponseEntity<SubredditResource> createSubreddit(@RequestBody SubredditResource subredditResource)
            throws ServerException, ClientException {
        log.info("Request received to create subreddit with subredditName {}", subredditResource.getName());
        Subreddit subredditSaved = subredditService.saveSubreddit(subredditConverter.toEntity(subredditResource));
        SubredditResource subredditResourceSaved = subredditConverter.toResource(subredditSaved);
        log.info("Request completed to create subreddit with subredditName {}", subredditResource.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(subredditResourceSaved);
    }

    @GetMapping("/{identifier}")
    public ResponseEntity<SubredditResource> getSingleSubreddit(@PathVariable @NotNull final String identifier) throws ServerException {
        log.info("Request received to get single subreddit with subredditId {}", identifier);
        Subreddit subreddit = subredditService.getSingleSubreddit(identifier);
        SubredditResource subredditResource = subredditConverter.toResource(subreddit);
        log.info("Request completed to get single subreddit with subredditId {}", identifier);
        return ResponseEntity.status(HttpStatus.OK).body(subredditResource);
    }

    @DeleteMapping("/{identifier}")
    public ResponseEntity<Void> deleteSubreddit(@PathVariable @NotNull final String identifier) throws ServerException {
        log.info("Request received to delete subreddit with subredditId {}", identifier);
        subredditService.deleteSubreddit(identifier);
        log.info("Request completed to delete subreddit with subredditId {}", identifier);
        return ResponseEntity.noContent().build();
    }
}
