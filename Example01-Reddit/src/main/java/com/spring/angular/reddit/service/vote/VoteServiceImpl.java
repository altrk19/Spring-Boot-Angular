package com.spring.angular.reddit.service.vote;

import com.spring.angular.reddit.constants.CommonConstants;
import com.spring.angular.reddit.constants.RequestErrorTypes;
import com.spring.angular.reddit.exception.ClientException;
import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.Post;
import com.spring.angular.reddit.model.Vote;
import com.spring.angular.reddit.model.VoteType;
import com.spring.angular.reddit.repository.VoteRepository;
import com.spring.angular.reddit.service.post.PostService;
import com.spring.angular.reddit.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VoteServiceImpl implements VoteService {
    private final VoteRepository voteRepository;
    private final PostService postService;
    private final UserService userService;

    public VoteServiceImpl(VoteRepository voteRepository, PostService postService,
                           UserService userService) {
        this.voteRepository = voteRepository;
        this.postService = postService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void addVote(Vote vote) throws ServerException, ClientException {
        Post post = postService.getSinglePost(vote.getPost().getPostId());
        Optional<Vote> voteByPostAndUser =
                voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, userService.getCurrentUser());
        if (voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(vote.getVoteType())) {
            throw new ClientException(RequestErrorTypes.GENERIC_POLICY_ERROR,
                    new String[]{CommonConstants.USER_ALREADY_VOTED, HttpStatus.FORBIDDEN.toString()},
                    HttpStatus.FORBIDDEN);
        }

        if (VoteType.UPVOTE.equals(vote.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }
        //bidirectioanal
        post.getVotes().add(vote);
        voteRepository.save(vote);
    }

}
