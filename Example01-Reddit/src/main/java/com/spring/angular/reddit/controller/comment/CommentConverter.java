package com.spring.angular.reddit.controller.comment;

import com.spring.angular.reddit.model.Comment;
import com.spring.angular.reddit.model.Post;
import com.spring.angular.reddit.resource.CommentListResource;
import com.spring.angular.reddit.resource.CommentResource;

import java.util.ArrayList;
import java.util.List;

public class CommentConverter {
    public static Comment toEntity(CommentResource commentResource){
        Comment comment = new Comment();
        comment.setCreatedDate(commentResource.getCreatedDate());
        comment.setText(commentResource.getText());

        Post post = new Post();
        post.setPostId(commentResource.getPostId());
        comment.setPost(post);

        return comment;
    }

    public static CommentResource toResource(Comment comment){
        return CommentResource.builder()
                .id(comment.getId())
                .postId(comment.getPost().getPostId())
                .createdDate(comment.getCreatedDate())
                .text(comment.getText())
                .userName(comment.getUser().getUsername())
                .build();
    }

    public static CommentListResource toResourceList(List<Comment> commentList){
        CommentListResource commentListResource = new CommentListResource(new ArrayList<>());
        commentListResource.setComments(commentList);
        return commentListResource;
    }
}
