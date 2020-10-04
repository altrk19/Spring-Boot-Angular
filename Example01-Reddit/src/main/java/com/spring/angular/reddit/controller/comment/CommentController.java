package com.spring.angular.reddit.controller.comment;

import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.Comment;
import com.spring.angular.reddit.resource.CommentResource;
import com.spring.angular.reddit.service.comment.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;
    private final CommentConverter commentConverter;

    public CommentController(CommentService commentService,
                             CommentConverter commentConverter) {
        this.commentService = commentService;
        this.commentConverter = commentConverter;
    }

    @GetMapping("/by-post/{postIdentifier}")
    public ResponseEntity<List<CommentResource>> getAllCommentsForPost(@PathVariable String postIdentifier)
            throws ServerException {
        log.info("Request received to get all comments for post with post Id {}", postIdentifier);
        List<Comment> comments = commentService.getAllCommentsForPost(postIdentifier);
        List<CommentResource> commentResources = commentConverter.toResourceList(comments);
        log.info("Request completed to get all comments for post with post Id {}", postIdentifier);
        return ResponseEntity.status(HttpStatus.OK).body(commentResources);
    }

    @GetMapping("/by-user/{userName}")
    public ResponseEntity<List<CommentResource>> getAllCommentsForUser(@PathVariable String userName)
            throws ServerException {
        log.info("Request received to get all comments for user with user name {}", userName);
        List<Comment> comments = commentService.getAllCommentsForUser(userName);
        List<CommentResource> commentResources = commentConverter.toResourceList(comments);
        log.info("Request completed to get all comments for user with user name {}", userName);
        return ResponseEntity.status(HttpStatus.OK).body(commentResources);
    }

    @PostMapping
    public ResponseEntity<Void> addComment(@RequestBody CommentResource commentResource) throws ServerException {
        log.info("Request received to add comment with username: {} and post Id {}", commentResource.getUserName(),
                commentResource.getPostId());
        commentService.saveComment(commentConverter.toEntity(commentResource));

        log.info("Request completed to add comment with username: {} and post Id {}", commentResource.getUserName(),
                commentResource.getPostId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}