package com.spring.angular.reddit.controller.post;

import com.spring.angular.reddit.resource.PostRequestResource;
import com.spring.angular.reddit.resource.PostResponseResource;
import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.Post;
import com.spring.angular.reddit.service.post.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequestResource postRequestResource) throws ServerException {
        Post post = PostConverter.toPostEntity(postRequestResource);
        postService.save(post);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostResponseResource>> getAllPosts() {
        return status(HttpStatus.OK).body(postService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseResource> getPost(@PathVariable Long id) throws ServerException {
        PostResponseResource postResponseResource = postService.getSinglePost(id);
        return status(HttpStatus.OK).body(postResponseResource);
    }

    @GetMapping("/by-subreddit/{id}")
    public ResponseEntity<List<PostResponseResource>> getPostsBySubreddit(Long id) {
        return status(HttpStatus.OK).body(postService.getPostsBySubreddit(id));
    }

    @GetMapping("/by-user/{name}")
    public ResponseEntity<List<PostResponseResource>> getPostsByUsername(String username) {
        return status(HttpStatus.OK).body(postService.getPostsByUsername(username));
    }
}