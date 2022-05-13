package com.spring.angular.reddit.repository;

import com.spring.angular.reddit.model.Comment;
import com.spring.angular.reddit.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
    List<Comment> findAllByUserId(Long userId);
    Optional<Comment> findByIdentifier(String identifier);
}