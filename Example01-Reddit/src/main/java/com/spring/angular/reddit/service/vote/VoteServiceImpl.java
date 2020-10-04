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
        Post post = postService.getSinglePost(vote.getPost().getIdentifier());

        checkUserAlreadyVoted(vote, post);

        vote.setIdentifier(KeyGenerationUtil.generateUniqueIdentifier());

        //bidirectioanal
        vote.setPost(post);
        post.getVotes().add(vote);
        post.setVoteCount(post.getVoteCount() + 1);

        User user = authenticationService.getCurrentUser();
        user.getVotes().add(vote);
        vote.setUser(user);

        voteRepository.save(vote);

        return vote;
    }

    @Override
    public Vote getVoteForPost(Post post, User currentUser) {
        return voteRepository.findByPostAndUser(post, currentUser).orElse(null);
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

    @Override
    public void deleteSingleVote(String identifier) throws ServerException {
        Vote vote = getSingleVote(identifier);
        Post post = vote.getPost();
        post.setVoteCount(post.getVoteCount() - 1);
        voteRepository.deleteById(vote.getVoteId());
    }

    @Override
    public Vote getSingleVote(String identifier) throws ServerException {
        return voteRepository.findByIdentifier(identifier).orElseThrow(() -> new ServerException(
                RequestErrorTypes.UNKNOWN_RESOURCE,
                new String[]{CommonConstants.VOTE, String.valueOf(String.valueOf(identifier))},
                HttpStatus.NOT_FOUND));
    }

    private void checkUserAlreadyVoted(Vote vote, Post post) throws ServerException, ClientException {
        Optional<Vote> voteByPostAndUser =
                voteRepository.findByPostAndUser(post, authenticationService.getCurrentUser());
        if (voteByPostAndUser.isPresent()) {
            if (voteByPostAndUser.get().getVoteType().equals(vote.getVoteType())) {
                log.debug("User {} already voted", authenticationService.getCurrentUser());
                throw new ClientException(RequestErrorTypes.GENERIC_POLICY_ERROR,
                        new String[]{CommonConstants.USER_ALREADY_VOTED, HttpStatus.FORBIDDEN.toString()},
                        HttpStatus.FORBIDDEN);
            } else {
                //already exist opposite vote, and we delete this
                Vote voteExist = voteByPostAndUser.get();
                deleteSingleVote(voteExist.getIdentifier());
                post.setVoteCount(post.getVoteCount() - 1);
            }
        }
    }
}
