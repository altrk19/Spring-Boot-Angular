package com.spring.angular.reddit.repository;

import com.spring.angular.reddit.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByPostIdAndUserId(Long postId, Long userId);

    Optional<Vote> findByIdentifier(String identifier);
}