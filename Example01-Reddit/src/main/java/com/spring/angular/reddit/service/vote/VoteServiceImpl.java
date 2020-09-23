package com.spring.angular.reddit.service.vote;

import com.spring.angular.reddit.dto.VoteDto;
import com.spring.angular.reddit.exception.PostNotFoundException;
import com.spring.angular.reddit.exception.SpringRedditException;
import com.spring.angular.reddit.model.Post;
import com.spring.angular.reddit.model.Vote;
import com.spring.angular.reddit.model.VoteType;
import com.spring.angular.reddit.repository.PostRepository;
import com.spring.angular.reddit.repository.VoteRepository;
import com.spring.angular.reddit.service.auth.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
public class VoteServiceImpl implements VoteService {
    private final PostRepository postRepository;
    private final VoteRepository voteRepository;
    private final AuthService authService;

    public VoteServiceImpl(PostRepository postRepository,
                           VoteRepository voteRepository, AuthService authService) {
        this.postRepository = postRepository;
        this.voteRepository = voteRepository;
        this.authService = authService;
    }

    @Override
    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));
        Optional<Vote> voteByPostAndUser =
                voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        if (voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())) {
            throw new SpringRedditException("You have already " + voteDto.getVoteType() + "'d for this post");
        }


        if (VoteType.UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }
        voteRepository.save(mapToVoteEntity(voteDto, post));
        postRepository.save(post);
    }

    private Vote mapToVoteEntity(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
