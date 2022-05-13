package com.spring.angular.reddit.repository;

import com.spring.angular.reddit.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends CrudRepository<Post, Long>,JpaRepository<Post, Long> {
    List<Post> findAllBySubredditId(Long id);
    List<Post> findByUserId(Long id);
    Optional<Post> findByIdentifier(String identifier);

//    @Transactional
//    @Modifying
//    @Query("Delete FROM Post p WHERE p.identifier=:identifier")
//    void deleteByIdentifier(@Param("identifier") String identifier);
}