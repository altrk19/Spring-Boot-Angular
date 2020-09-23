package com.spring.angular.reddit.service.subreddit;

import com.spring.angular.reddit.dto.SubredditDto;
import com.spring.angular.reddit.exception.SpringRedditException;
import com.spring.angular.reddit.model.Subreddit;
import com.spring.angular.reddit.repository.SubredditRepository;
import com.spring.angular.reddit.service.auth.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubredditServiceImpl implements SubredditService {
    private final SubredditRepository subredditRepository;
    private final AuthService authService;

    public SubredditServiceImpl(SubredditRepository subredditRepository,
                                AuthService authService) {
        this.subredditRepository = subredditRepository;
        this.authService = authService;
    }

    @Override
    @Transactional
    public SubredditDto saveSubreddit(SubredditDto subredditDto) {
        Subreddit savedSubreddit = subredditRepository.save(mapSubredditEntity(subredditDto));
        subredditDto.setId(savedSubreddit.getId());
        return subredditDto;
    }

    @Override
    public List<SubredditDto> getAllSubreddits() {
        return subredditRepository.findAll()
                .stream()
                .map(this::mapToSubredditDto)
                .collect(Collectors.toList());
    }

    @Override
    public SubredditDto getSingleSubreddit(Long id) {
        return mapToSubredditDto(subredditRepository.findById(id).orElseThrow(() -> new SpringRedditException("User not found - " + id)));

    }

    private SubredditDto mapToSubredditDto(Subreddit subreddit){
        return SubredditDto.builder()
                .id(subreddit.getId())
                .name(subreddit.getName())
                .description(subreddit.getDescription())
                .numberOfPosts(subreddit.getPosts().size())
                .build();
    }

    private Subreddit mapSubredditEntity(SubredditDto subredditDto) {
        return Subreddit.builder().
                name(subredditDto.getName())
                .description(subredditDto.getDescription())
                .createdDate(Instant.now())
                .user(authService.getCurrentUser())
                .build();
    }
}
