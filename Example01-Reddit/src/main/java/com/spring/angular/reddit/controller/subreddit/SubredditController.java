package com.spring.angular.reddit.controller.subreddit;

import com.spring.angular.reddit.resource.SubredditResource;
import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.service.subreddit.SubredditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@Slf4j
public class SubredditController {

    private final SubredditService subredditService;

    public SubredditController(SubredditService subredditService) {
        this.subredditService = subredditService;
    }

    @PostMapping
    public ResponseEntity<SubredditResource> createSubreddit(@RequestBody SubredditResource subredditResource){
        SubredditResource savedSubreddit = subredditService.saveSubreddit(subredditResource);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSubreddit);
    }

    @GetMapping
    public ResponseEntity<List<SubredditResource>> getAllSubreddits(){
        List<SubredditResource> subreddits = subredditService.getAllSubreddits();
        return ResponseEntity.status(HttpStatus.OK).body(subreddits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubredditResource> getSingleSubreddit(@PathVariable Long id) throws ServerException {
        SubredditResource subreddit = subredditService.getSingleSubreddit(id);
        return ResponseEntity.status(HttpStatus.OK).body(subreddit);
    }
}
