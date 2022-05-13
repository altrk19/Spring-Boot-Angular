package com.spring.angular.reddit.service.subreddit;

import com.spring.angular.reddit.constants.CommonConstants;
import com.spring.angular.reddit.constants.RequestErrorTypes;
import com.spring.angular.reddit.exception.ClientException;
import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.Subreddit;
import com.spring.angular.reddit.repository.SubredditRepository;
import com.spring.angular.reddit.service.auth.AuthenticationService;
import com.spring.angular.reddit.util.KeyGenerationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
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
    public Subreddit saveSubreddit(Subreddit subreddit) throws ServerException, ClientException {
        checkSubredditIsDuplicate(subreddit.getName());

        subreddit.setUser(authenticationService.getCurrentUser());
        subreddit.setIdentifier(KeyGenerationUtil.generateUniqueIdentifier());
        return subredditRepository.save(subreddit);
    }

    private void checkSubredditIsDuplicate(String subredditName) throws ClientException {
        Subreddit subreddit = subredditRepository.findByName(subredditName).orElse(null);
        if (Objects.nonNull(subreddit)) {
            log.debug("already exist subreddit name : {}", subredditName);
            throw new ClientException(RequestErrorTypes.GENERIC_POLICY_ERROR,
                    new String[]{CommonConstants.ALREADY_EXIST_SUBREDDIT, HttpStatus.FORBIDDEN.toString()},
                    HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public Subreddit getSingleSubreddit(String identifier) throws ServerException {
        return subredditRepository.findByIdentifier(identifier).orElseThrow(
                () -> new ServerException(RequestErrorTypes.UNKNOWN_RESOURCE,
                        new String[]{CommonConstants.SUB_REDDIT, String.valueOf(String.valueOf(identifier))},
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
    public void deleteSubreddit(String identifier) throws ServerException {
        Subreddit subreddit = getSingleSubreddit(identifier);
        subredditRepository.deleteById(subreddit.getId());
    }
}
