package com.spring.angular.reddit.service.comment;


import com.spring.angular.reddit.dto.CommentsDto;
import com.spring.angular.reddit.dto.NotificationEmail;
import com.spring.angular.reddit.exception.PostNotFoundException;
import com.spring.angular.reddit.model.Comment;
import com.spring.angular.reddit.model.Post;
import com.spring.angular.reddit.model.User;
import com.spring.angular.reddit.repository.CommentRepository;
import com.spring.angular.reddit.repository.PostRepository;
import com.spring.angular.reddit.repository.UserRepository;
import com.spring.angular.reddit.service.auth.AuthService;
import com.spring.angular.reddit.service.mail.MailContentBuilder;
import com.spring.angular.reddit.service.mail.MailService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CommentServiceImpl implements CommentService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public CommentServiceImpl(PostRepository postRepository, UserRepository userRepository,
                              AuthService authService, CommentRepository commentRepository,
                              MailContentBuilder mailContentBuilder,
                              MailService mailService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.authService = authService;
        this.commentRepository = commentRepository;
        this.mailContentBuilder = mailContentBuilder;
        this.mailService = mailService;
    }

    @Override
    public void saveComment(CommentsDto commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));

        User currentUser = authService.getCurrentUser();

        Comment comment = mapCommentEntity(commentsDto, post, currentUser);
        commentRepository.save(comment);

        String message =
                mailContentBuilder.build(currentUser.getUsername() + " posted a comment on your post." );
        sendCommentNotification(message, post.getUser());
    }

    @Override
    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(this::mapToCommentsDto).collect(Collectors.toList());
    }

    @Override
    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(this::mapToCommentsDto)
                .collect(Collectors.toList());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(
                new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
    }

    private Comment mapCommentEntity(CommentsDto commentsDto, Post post, User user) {
        return Comment.builder()
                .text(commentsDto.getText())
                .post(post)
                .createdDate(Instant.now())
                .user(user)
                .build();
    }

    private CommentsDto mapToCommentsDto(Comment comment) {
        return CommentsDto.builder()
                .id(comment.getId())
                .postId(comment.getPost().getPostId())
                .createdDate(comment.getCreatedDate())
                .text(comment.getText())
                .userName(comment.getUser().getUsername())
                .build();
    }

}
