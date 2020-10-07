package com.spring.angular.reddit.service.post;

import com.spring.angular.reddit.constants.CommonConstants;
import com.spring.angular.reddit.constants.RequestErrorTypes;
import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.Post;
import com.spring.angular.reddit.model.Subreddit;
import com.spring.angular.reddit.model.User;
import com.spring.angular.reddit.repository.PostRepository;
import com.spring.angular.reddit.service.auth.AuthenticationService;
import com.spring.angular.reddit.service.subreddit.SubredditService;
import com.spring.angular.reddit.util.KeyGenerationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final SubredditService subredditService;
    private final AuthenticationService authenticationService;

    public PostServiceImpl(PostRepository postRepository,
                           SubredditService subredditService,
                           AuthenticationService authenticationService) {
        this.postRepository = postRepository;
        this.subredditService = subredditService;
        this.authenticationService = authenticationService;
    }

    @Transactional
    @Override
    public Post savePost(Post post) throws ServerException {
        //bidirectional
        Subreddit subreddit = subredditService.getSingleSubredditByName(post.getSubreddit().getName());

        post.setIdentifier(KeyGenerationUtil.generateUniqueIdentifier());

        post.setSubreddit(subreddit);
        subreddit.getPosts().add(post);

        User user = authenticationService.getCurrentUser();
        user.getPosts().add(post);
        post.setUser(user);

        postRepository.save(post);

        log.debug("Post created successfully under subreddit : " + subreddit.getName());
        return post;
    }


    @Override
    public Post getSinglePost(String identifier) throws ServerException {
        return postRepository.findByIdentifier(identifier).orElseThrow(() -> new ServerException(RequestErrorTypes.UNKNOWN_RESOURCE,
                new String[]{CommonConstants.POST, String.valueOf(String.valueOf(identifier))}, HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> getAllPostsBySubredditIdentifier(String subredditIdentifier) throws ServerException {
        Subreddit subreddit = subredditService.getSingleSubreddit(subredditIdentifier);
        return postRepository.findAllBySubreddit(subreddit);
    }

    @Override
    public List<Post> getAllPostsByUsername(String username) throws ServerException {
        User user = authenticationService.getUserByUsername(username);
        return postRepository.findByUser(user);
    }

    @Override
    public void deleteSinglePost(String identifier) throws ServerException {
        Post post = getSinglePost(identifier);
        postRepository.deleteById(post.getId());
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
