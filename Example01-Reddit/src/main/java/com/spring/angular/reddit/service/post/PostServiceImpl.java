package com.spring.angular.reddit.service.post;

import com.spring.angular.reddit.constants.CommonConstants;
import com.spring.angular.reddit.constants.RequestErrorTypes;
import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.*;
import com.spring.angular.reddit.repository.PostRepository;
import com.spring.angular.reddit.repository.VoteRepository;
import com.spring.angular.reddit.service.subreddit.SubredditService;
import com.spring.angular.reddit.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final SubredditService subredditService;
    private final UserService userService;
    private final VoteRepository voteRepository;

    public PostServiceImpl(PostRepository postRepository,
                           SubredditService subredditService,
                           UserService userService, VoteRepository voteRepository) {
        this.postRepository = postRepository;
        this.subredditService = subredditService;
        this.userService = userService;
        this.voteRepository = voteRepository;
    }

    @Transactional
    @Override
    public void save(Post post) throws ServerException {
        Subreddit subreddit = subredditService.getSingleSubredditByName(post.getSubreddit().getName());

        // bi directonal
        subreddit.getPosts().add(post);
        postRepository.save(post);
        log.debug("Post created successfully under subreddit : " + subreddit.getName());
    }


    @Override
    public Post getSinglePost(Long id) throws ServerException {
        return postRepository.findById(id).orElseThrow(() -> new ServerException(RequestErrorTypes.UNKNOWN_RESOURCE,
                new String[]{CommonConstants.POST, String.valueOf(String.valueOf(id))}, HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> getPostsBySubreddit(Long subredditId) throws ServerException {
        Subreddit subreddit = subredditService.getSingleSubreddit(subredditId);
        return postRepository.findAllBySubreddit(subreddit);
    }

    @Override
    public List<Post> getPostsByUsername(String username) throws ServerException {
        User user = userService.getUserByUsername(username);
        return postRepository.findByUser(user);
    }

    private Boolean checkVoteType(Post post, VoteType voteType) throws ServerException {
        if (userService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser =
                    voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, userService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType)).isPresent();
        }
        return false;
    }

//    private PostResponseDto mapToPostDto(Post post) throws ServerException {
//        return PostResponseDto.builder()
//                .id(post.getPostId())
//                .postName(post.getPostName())
//                .url(post.getUrl())
//                .description(post.getDescription())
//                .userName(post.getUser().getUsername())
//                .subredditName(post.getSubreddit().getName())
//                .commentCount(commentRepository.findByPost(post).size())
//                .duration(String.valueOf(post.getCreatedDate().toEpochMilli()))
//                .upVote(checkVoteType(post, VoteType.UPVOTE))
//                .downVote(checkVoteType(post, VoteType.DOWNVOTE))
//                .voteCount(post.getVoteCount())
//                .build();
//
//
//    }
//
//    private Post mapToPostEntity(PostRequestDto postRequestDto, Subreddit subreddit, User currentUser) {
//        return Post.builder().
//                postName(postRequestDto.getPostName())
//                .url(postRequestDto.getUrl())
//                .description(postRequestDto.getDescription())
//                .user(currentUser)
//                .createdDate(Instant.now())
//                .subreddit(subreddit)
//                .build();
//    }


}
