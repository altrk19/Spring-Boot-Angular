package com.spring.angular.reddit.controller;

import com.spring.angular.reddit.dto.SubredditDto;
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
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto){
        SubredditDto savedSubreddit = subredditService.saveSubreddit(subredditDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSubreddit);
    }

    @GetMapping
    public ResponseEntity<List<SubredditDto>> getAllSubreddits(){
        List<SubredditDto> subreddits = subredditService.getAllSubreddits();
        return ResponseEntity.status(HttpStatus.OK).body(subreddits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubredditDto> getSingleSubreddit(@PathVariable Long id){
        SubredditDto subreddit = subredditService.getSingleSubreddit(id);
        return ResponseEntity.status(HttpStatus.OK).body(subreddit);
    }
}
