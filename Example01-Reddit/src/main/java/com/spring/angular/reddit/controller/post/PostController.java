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

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

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
        return status(HttpStatus.OK).body(postResponseResources);
    }

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequestResource postRequestResource)
            throws ServerException {
        log.info("Request received to create post for postId {}", postRequestResource.getPostId());
        Post post = postConverter.toPostEntity(postRequestResource);
        postService.save(post);
        log.info("Request completed to create post for postId {}", postRequestResource.getPostId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseResource> getSinglePost(@PathVariable Long id) throws ServerException {
        log.info("Request received to get single post for postId {}", id);
        Post post = postService.getSinglePost(id);

        int commentCount = commentService.getAllCommentsForPost(post.getPostId()).size();
        VoteType voteType = voteService.getVoteType(post);
        PostResponseResource postResponseResource = postConverter.toResource(post, commentCount, voteType);

        log.info("Request completed to get single post for postId {}", id);
        return status(HttpStatus.OK).body(postResponseResource);
    }

    @GetMapping("/by-subreddit/{identifier}")
    public ResponseEntity<List<PostResponseResource>> getAllPostsBySubreddit(String identifier) throws ServerException {
        log.info("Request received to get all posts for subreddit with subredditId {}", identifier);
        List<Post> posts = postService.getPostsBySubreddit(identifier);
        List<PostResponseResource> postResponseResources = postConverter.toResourceList(posts);
        log.info("Request completed to get all posts for subreddit with subredditId {}", identifier);
        return status(HttpStatus.OK).body(postResponseResources);
    }

    @GetMapping("/by-user/{name}")
    public ResponseEntity<List<PostResponseResource>> getAllPostsByUsername(String username) throws ServerException {
        log.info("Request received to get all posts for user with username {}", username);
        List<Post> posts = postService.getPostsByUsername(username);
        List<PostResponseResource> postResponseResources = postConverter.toResourceList(posts);
        log.info("Request received to get all posts for user with username {}", username);
        return status(HttpStatus.OK).body(postResponseResources);
    }
}