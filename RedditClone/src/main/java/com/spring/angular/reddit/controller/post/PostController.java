package com.spring.angular.reddit.controller.post;

import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.Post;
import com.spring.angular.reddit.model.VoteType;
import com.spring.angular.reddit.resource.PostRequestResource;
import com.spring.angular.reddit.resource.PostResponseResource;
import com.spring.angular.reddit.service.comment.CommentService;
import com.spring.angular.reddit.service.post.PostService;
import com.spring.angular.reddit.service.vote.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Slf4j
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final VoteService voteService;
    private final PostConverter postConverter;

    public PostController(PostService postService, CommentService commentService,
                          VoteService voteService, PostConverter postConverter) {
        this.postService = postService;
        this.commentService = commentService;
        this.voteService = voteService;
        this.postConverter = postConverter;
    }

    @GetMapping
    public ResponseEntity<List<PostResponseResource>> getAllPosts() throws ServerException {
        log.info("Request received to get all posts");
        List<Post> posts = postService.getAllPosts();
        List<PostResponseResource> postResponseResources = postConverter.toResourceList(posts);
        log.info("Request completed to get all posts");
        return ResponseEntity.status(HttpStatus.OK).body(postResponseResources);
    }

    @PostMapping
    public ResponseEntity<PostResponseResource> createPost(@RequestBody PostRequestResource postRequestResource)
            throws ServerException {
        log.info("Request received to create post for postId {}", postRequestResource.getPostId());
        Post postSaved = postService.savePost(postConverter.toPostEntity(postRequestResource));
        PostResponseResource postResponseResource = postConverter.toResource(postSaved, 0, null);
        log.info("Request completed to create post for postId {}", postRequestResource.getPostId());
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponseResource);
    }

    @GetMapping("/{identifier}")
    public ResponseEntity<PostResponseResource> getSinglePost(@PathVariable String identifier) throws ServerException {
        log.info("Request received to get single post for postId {}", identifier);
        Post post = postService.getSinglePost(identifier);

        int commentCount = commentService.getAllCommentsForPost(post.getIdentifier()).size();
        VoteType voteType = voteService.getVoteType(post);
        PostResponseResource postResponseResource = postConverter.toResource(post, commentCount, voteType);

        log.info("Request completed to get single post for postId {}", identifier);
        return ResponseEntity.status(HttpStatus.OK).body(postResponseResource);
    }

    @GetMapping("/by-subreddit/{subredditIdentifier}")
    public ResponseEntity<List<PostResponseResource>> getAllPostsBySubredditIdentifier(
            @PathVariable @NotNull final String subredditIdentifier) throws ServerException {
        log.info("Request received to get all posts for subreddit with subreddit identifier {}", subredditIdentifier);
        List<Post> posts = postService.getAllPostsBySubredditIdentifier(subredditIdentifier);
        List<PostResponseResource> postResponseResources = postConverter.toResourceList(posts);
        log.info("Request completed to get all posts for subreddit with subredditId {}", subredditIdentifier);
        return ResponseEntity.status(HttpStatus.OK).body(postResponseResources);
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<List<PostResponseResource>> getAllPostsByUsername(
            @PathVariable @NotNull final String username) throws ServerException {
        log.info("Request received to get all posts for user with username {}", username);
        List<Post> posts = postService.getAllPostsByUsername(username);
        List<PostResponseResource> postResponseResources = postConverter.toResourceList(posts);
        log.info("Request received to get all posts for user with username {}", username);
        return ResponseEntity.status(HttpStatus.OK).body(postResponseResources);
    }

    @DeleteMapping("/{identifier}")
    public ResponseEntity<Void> deleteSinglePost(@PathVariable String identifier)
            throws ServerException {
        log.info("Request received to delete single post for postId {}", identifier);
        postService.deleteSinglePost(identifier);
        log.info("Request completed to delete single post for postId {}", identifier);
        return ResponseEntity.noContent().build();
    }
}