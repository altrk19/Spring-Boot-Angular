package com.spring.angular.reddit.repository;

import com.spring.angular.reddit.model.UserActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<UserActivationToken, Long> {
    Optional<UserActivationToken> findByToken(String token);
}