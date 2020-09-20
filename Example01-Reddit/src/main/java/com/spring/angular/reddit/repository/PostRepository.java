package com.spring.angular.reddit.repository;

import com.spring.angular.reddit.model.Post;
import com.spring.angular.reddit.model.Subreddit;
import com.spring.angular.reddit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);
    List<Post> findByUser(User user);
}