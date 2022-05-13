package com.spring.angular.reddit.repository;

import com.spring.angular.reddit.model.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubredditRepository extends JpaRepository<Subreddit, Long> {
    Optional<Subreddit> findByName(String subredditName);
    Optional<Subreddit> findByIdentifier(String identifier);
}