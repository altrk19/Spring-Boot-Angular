package com.spring.angular.reddit.service.comment;

import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.Comment;

import java.util.List;

public interface CommentService {
    Comment saveComment(Comment comment) throws ServerException;

    List<Comment> getAllCommentsForPost(String postIdentifier) throws ServerException;

    List<Comment> getAllCommentsForUser(String userName) throws ServerException;

    Comment getSingleComment(String identifier) throws ServerException;

    void deleteSingleComment(String identifier) throws ServerException;
}
