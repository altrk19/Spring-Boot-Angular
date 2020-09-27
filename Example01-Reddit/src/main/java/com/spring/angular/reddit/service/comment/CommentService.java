package com.spring.angular.reddit.service.comment;

import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.Comment;
import com.spring.angular.reddit.model.Post;

import java.util.List;

public interface CommentService {
    void saveComment(Comment comment) throws ServerException;

    List<Comment> getAllCommentsForPost(Long postId) throws ServerException;

    List<Comment> getAllCommentsForUser(String userName) throws ServerException;

}
