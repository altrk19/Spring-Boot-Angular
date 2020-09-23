package com.spring.angular.reddit.service.comment;

import com.spring.angular.reddit.dto.CommentsDto;

import java.util.List;

public interface CommentService {
    void saveComment(CommentsDto commentsDto);
    List<CommentsDto> getAllCommentsForPost(Long postId);
    List<CommentsDto> getAllCommentsForUser(String userName);
}
