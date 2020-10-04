package com.spring.angular.reddit.service.subreddit;

import com.spring.angular.reddit.constants.CommonConstants;
import com.spring.angular.reddit.constants.RequestErrorTypes;
import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.Subreddit;
import com.spring.angular.reddit.model.User;
import com.spring.angular.reddit.repository.SubredditRepository;
import com.spring.angular.reddit.service.auth.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubredditServiceImpl implements SubredditService {
    private final SubredditRepository subredditRepository;
    private final AuthenticationService authenticationService;

    public SubredditServiceImpl(SubredditRepository subredditRepository,
                                AuthenticationService authenticationService) {
        this.subredditRepository = subredditRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public List<Subreddit> getAllSubreddits() {
        return subredditRepository.findAll();
    }

    @Override
    @Transactional
    public Subreddit saveSubreddit(Subreddit subreddit) throws ServerException {
        subreddit.setUser(authenticationService.getCurrentUser());
        return subredditRepository.save(subreddit);
    }

    @Override
    public Subreddit getSingleSubreddit(Long id) throws ServerException {
        return subredditRepository.findById(id).orElseThrow(
                () -> new ServerException(RequestErrorTypes.UNKNOWN_RESOURCE,
                        new String[]{CommonConstants.SUB_REDDIT, String.valueOf(String.valueOf(id))},
                        HttpStatus.NOT_FOUND));
    }

    @Override
    public Subreddit getSingleSubredditByName(String name) throws ServerException {
        return subredditRepository.findByName(name).orElseThrow(() -> new ServerException(
                RequestErrorTypes.UNKNOWN_RESOURCE,
                new String[]{CommonConstants.SUB_REDDIT, String.valueOf(String.valueOf(name))},
                HttpStatus.NOT_FOUND));
    }

    @Override
    public void deleteSubreddit(Long id) {
        subredditRepository.deleteById(id);
    }
}
