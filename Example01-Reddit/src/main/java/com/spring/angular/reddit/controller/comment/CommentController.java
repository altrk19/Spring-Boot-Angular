package com.spring.angular.reddit.controller.comment;

import com.spring.angular.reddit.resource.CommentListResource;
import com.spring.angular.reddit.resource.CommentResource;
import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.Comment;
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

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<Void> addComment(@RequestBody CommentResource commentResource) throws ServerException {
        log.info("Request received to add comment with username: {} and post Id {}", commentResource.getUserName(),
                commentResource.getPostId());
        commentService.saveComment(CommentConverter.toEntity(commentResource));

        log.info("Request completed to add comment with username: {} and post Id {}", commentResource.getUserName(),
                commentResource.getPostId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/by-post/{postId}")
    public ResponseEntity<CommentListResource> getAllCommentsForPost(@PathVariable Long postId) throws ServerException {
        List<Comment> comments = commentService.getAllCommentsForPost(postId);
        CommentListResource commentListResource = CommentConverter.toResourceList(comments);
        return ResponseEntity.status(HttpStatus.OK).body(commentListResource);
    }

    @GetMapping("/by-user/{userName}")
    public ResponseEntity<CommentListResource> getAllCommentsForUser(@PathVariable String userName)
            throws ServerException {
        List<Comment> comments = commentService.getAllCommentsForUser(userName);
        CommentListResource commentListResource = CommentConverter.toResourceList(comments);
        return ResponseEntity.status(HttpStatus.OK).body(commentListResource);
    }

}