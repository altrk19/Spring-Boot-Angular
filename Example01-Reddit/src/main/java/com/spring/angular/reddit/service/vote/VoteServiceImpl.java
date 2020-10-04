package com.spring.angular.reddit.service.vote;

import com.spring.angular.reddit.constants.CommonConstants;
import com.spring.angular.reddit.constants.RequestErrorTypes;
import com.spring.angular.reddit.exception.ClientException;
import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.Post;
import com.spring.angular.reddit.model.User;
import com.spring.angular.reddit.model.Vote;
import com.spring.angular.reddit.model.VoteType;
import com.spring.angular.reddit.repository.VoteRepository;
import com.spring.angular.reddit.service.auth.AuthenticationService;
import com.spring.angular.reddit.service.post.PostService;
import com.spring.angular.reddit.util.KeyGenerationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class VoteServiceImpl implements VoteService {
    private final VoteRepository voteRepository;
    private final PostService postService;
    private final AuthenticationService authenticationService;

    public VoteServiceImpl(VoteRepository voteRepository, PostService postService,
                           AuthenticationService authenticationService) {
        this.voteRepository = voteRepository;
        this.postService = postService;
        this.authenticationService = authenticationService;
    }

    @Override
    @Transactional
    public Vote addVote(Vote vote) throws ServerException, ClientException {
        vote.setIdentifier(KeyGenerationUtil.generateUniqueIdentifier());


        Post post = postService.getSinglePost(vote.getPost().getIdentifier());

        checkUserAlreadyVoted(vote, post);

        //bidirectioanal
        vote.setPost(post);
        post.getVotes().add(vote);

        User user = authenticationService.getCurrentUser();
        user.getVotes().add(vote);
        vote.setUser(user);



        if (VoteType.UPVOTE.equals(vote.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }

        voteRepository.save(vote);

        return vote;
    }

    @Override
    public Vote getVoteForPost(Post post, User currentUser) {
        return voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, currentUser).orElse(null);
    }

    @Override
    public VoteType getVoteType(Post post) throws ServerException {
        if (authenticationService.isLoggedIn()) {
            Vote voteForPostByUser = getVoteForPost(post, authenticationService.getCurrentUser());
            if (Objects.nonNull(voteForPostByUser)) {
                return voteForPostByUser.getVoteType();
            }
        }
        return null;
    }

    private void checkUserAlreadyVoted(Vote vote, Post post) throws ServerException, ClientException {
        Optional<Vote> voteByPostAndUser =
                voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authenticationService.getCurrentUser());
        if (voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(vote.getVoteType())) {
            log.debug("User {} already voted", authenticationService.getCurrentUser());
            throw new ClientException(RequestErrorTypes.GENERIC_POLICY_ERROR,
                    new String[]{CommonConstants.USER_ALREADY_VOTED, HttpStatus.FORBIDDEN.toString()},
                    HttpStatus.FORBIDDEN);
        }
    }
}
