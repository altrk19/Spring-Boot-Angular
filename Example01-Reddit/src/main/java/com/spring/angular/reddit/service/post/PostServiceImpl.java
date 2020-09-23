package com.spring.angular.reddit.service.post;

import com.spring.angular.reddit.dto.PostRequest;
import com.spring.angular.reddit.dto.PostResponse;
import com.spring.angular.reddit.exception.PostNotFoundException;
import com.spring.angular.reddit.exception.SubredditNotFoundException;
import com.spring.angular.reddit.model.Post;
import com.spring.angular.reddit.model.Subreddit;
import com.spring.angular.reddit.model.User;
import com.spring.angular.reddit.repository.PostRepository;
import com.spring.angular.reddit.repository.SubredditRepository;
import com.spring.angular.reddit.repository.UserRepository;
import com.spring.angular.reddit.service.auth.AuthService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    public PostServiceImpl(PostRepository postRepository,
                           SubredditRepository subredditRepository,
                           UserRepository userRepository, AuthService authService) {
        this.postRepository = postRepository;
        this.subredditRepository = subredditRepository;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @Transactional
    @Override
    public void save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
        postRepository.save(mapToPostEntity(postRequest, subreddit, authService.getCurrentUser()));
    }


    @Override
    public PostResponse getSinglePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return mapToPostDto(post);
    }

    @Override
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::mapToPostDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(this::mapToPostDto).collect(Collectors.toList());
    }

    @Override
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(this::mapToPostDto)
                .collect(Collectors.toList());
    }

    private PostResponse mapToPostDto(Post post) {
        return PostResponse.builder()
                .id(post.getPostId())
                .postName(post.getPostName())
                .url(post.getUrl())
                .description(post.getDescription())
                .userName(post.getUser().getUsername())
                .subredditName(post.getSubreddit().getName())
                .build();
    }
    private Post mapToPostEntity(PostRequest postRequest, Subreddit subreddit, User currentUser) {
        return Post.builder().
                postName(postRequest.getPostName())
                .url(postRequest.getUrl())
                .description(postRequest.getDescription())
                .user(currentUser)
                .subreddit(subreddit)
                .build();
    }
}
