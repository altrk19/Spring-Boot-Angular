package com.spring.angular.reddit.service.post;

import com.spring.angular.reddit.dto.PostRequestDto;
import com.spring.angular.reddit.dto.PostResponseDto;
import com.spring.angular.reddit.exception.PostNotFoundException;
import com.spring.angular.reddit.exception.SubredditNotFoundException;
import com.spring.angular.reddit.model.*;
import com.spring.angular.reddit.repository.*;
import com.spring.angular.reddit.service.auth.AuthService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository;

    public PostServiceImpl(PostRepository postRepository,
                           SubredditRepository subredditRepository,
                           UserRepository userRepository, AuthService authService,
                           CommentRepository commentRepository,
                           VoteRepository voteRepository) {
        this.postRepository = postRepository;
        this.subredditRepository = subredditRepository;
        this.userRepository = userRepository;
        this.authService = authService;
        this.commentRepository = commentRepository;
        this.voteRepository = voteRepository;
    }

    @Transactional
    @Override
    public void save(PostRequestDto postRequestDto) {
        Subreddit subreddit = subredditRepository.findByName(postRequestDto.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException(postRequestDto.getSubredditName()));
        Post savedPost = mapToPostEntity(postRequestDto, subreddit, authService.getCurrentUser());
        savedPost.getSubreddit().getPosts().add(savedPost);
        postRepository.save(savedPost);
    }


    @Override
    public PostResponseDto getSinglePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return mapToPostDto(post);
    }

    @Override
    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::mapToPostDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostResponseDto> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(this::mapToPostDto).collect(Collectors.toList());
    }

    @Override
    public List<PostResponseDto> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(this::mapToPostDto)
                .collect(Collectors.toList());
    }

    private PostResponseDto mapToPostDto(Post post) {
        return PostResponseDto.builder()
                .id(post.getPostId())
                .postName(post.getPostName())
                .url(post.getUrl())
                .description(post.getDescription())
                .userName(post.getUser().getUsername())
                .subredditName(post.getSubreddit().getName())
                .commentCount(commentRepository.findByPost(post).size())
                .duration(String.valueOf(post.getCreatedDate().toEpochMilli()))
                .upVote(checkVoteType(post, VoteType.UPVOTE))
                .downVote(checkVoteType(post, VoteType.DOWNVOTE))
                .voteCount(post.getVoteCount())
                .build();


    }

    private Post mapToPostEntity(PostRequestDto postRequestDto, Subreddit subreddit, User currentUser) {
        return Post.builder().
                postName(postRequestDto.getPostName())
                .url(postRequestDto.getUrl())
                .description(postRequestDto.getDescription())
                .user(currentUser)
                .createdDate(Instant.now())
                .subreddit(subreddit)
                .build();
    }

    private Boolean checkVoteType(Post post, VoteType voteType) {
        if (authService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser =
                    voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType)).isPresent();
        }
        return false;
    }
}
