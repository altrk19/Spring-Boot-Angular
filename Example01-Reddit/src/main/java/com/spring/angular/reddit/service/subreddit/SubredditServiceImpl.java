package com.spring.angular.reddit.service.subreddit;

import com.spring.angular.reddit.constants.CommonConstants;
import com.spring.angular.reddit.constants.RequestErrorTypes;
import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.Subreddit;
import com.spring.angular.reddit.repository.SubredditRepository;
import com.spring.angular.reddit.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubredditServiceImpl implements SubredditService {
    private final SubredditRepository subredditRepository;
    private final UserService userService;

    public SubredditServiceImpl(SubredditRepository subredditRepository,
                                UserService userService) {
        this.subredditRepository = subredditRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public Subreddit saveSubreddit(Subreddit subreddit) {
        return subredditRepository.save(subreddit);
    }

    @Override
    public List<Subreddit> getAllSubreddits() {
        return subredditRepository.findAll();
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
}
