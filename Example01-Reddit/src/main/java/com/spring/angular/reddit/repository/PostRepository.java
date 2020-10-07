package com.spring.angular.reddit.repository;

import com.spring.angular.reddit.model.Post;
import com.spring.angular.reddit.model.Subreddit;
import com.spring.angular.reddit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends CrudRepository<Post, Long>,JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);
    List<Post> findByUser(User user);
    Optional<Post> findByIdentifier(String identifier);

//    @Transactional
//    @Modifying
//    @Query("Delete FROM Post p WHERE p.identifier=:identifier")
//    void deleteByIdentifier(@Param("identifier") String identifier);
}