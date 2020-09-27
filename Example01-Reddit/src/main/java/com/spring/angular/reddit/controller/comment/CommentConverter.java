package com.spring.angular.reddit.controller.comment;

import com.spring.angular.reddit.model.Comment;
import com.spring.angular.reddit.model.Post;
import com.spring.angular.reddit.resource.CommentResource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CommentConverter {
    public Comment toEntity(CommentResource commentResource) {
        Comment comment = new Comment();
        comment.setCreatedDate(commentResource.getCreatedDate());
        comment.setText(commentResource.getText());

        Post post = new Post();
        post.setPostId(commentResource.getPostId());
        comment.setPost(post);

        return comment;
    }

    public CommentResource toResource(Comment comment) {
        return CommentResource.builder()
                .id(comment.getId())
                .postId(comment.getPost().getPostId())
                .createdDate(comment.getCreatedDate())
                .text(comment.getText())
                .userName(comment.getUser().getUsername())
                .build();
    }

    public List<CommentResource> toResourceList(List<Comment> comments) {
        return comments.stream().filter(Objects::nonNull).map(this::toResource)
                .collect(Collectors.toList());
    }
}
