package com.spring.angular.reddit.repository;

import com.spring.angular.reddit.model.RefreshToken;
import com.spring.angular.reddit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
//    void deleteByUser(User user);
}