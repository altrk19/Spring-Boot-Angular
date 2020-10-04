package com.spring.angular.reddit.controller.comment;

import com.spring.angular.reddit.model.Comment;
import com.spring.angular.reddit.model.Post;
import com.spring.angular.reddit.resource.CommentResource;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CommentConverter {
    public Comment toEntity(CommentResource commentResource, String postIdentifier) {
        Comment comment = new Comment();
        comment.setText(commentResource.getText());

        Post post = new Post();
        post.setIdentifier(postIdentifier);
        comment.setPost(post);

        return comment;
    }

    public CommentResource toResource(Comment comment) {
        return CommentResource.builder()
                .identifier(comment.getIdentifier())
                .postIdentifier(comment.getPost().getIdentifier())
                .createdDate(comment.getCreatedDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .text(comment.getText())
                .build();
    }

    public List<CommentResource> toResourceList(List<Comment> comments) {
        return comments.stream().filter(Objects::nonNull).map(this::toResource)
                .collect(Collectors.toList());
    }
}
