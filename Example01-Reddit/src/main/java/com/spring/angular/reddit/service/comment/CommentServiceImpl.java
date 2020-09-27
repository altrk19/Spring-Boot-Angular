package com.spring.angular.reddit.service.comment;


import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.Comment;
import com.spring.angular.reddit.model.Post;
import com.spring.angular.reddit.model.User;
import com.spring.angular.reddit.repository.CommentRepository;
import com.spring.angular.reddit.resource.NotificationEmailResource;
import com.spring.angular.reddit.service.auth.AuthenticationService;
import com.spring.angular.reddit.service.mail.MailContentBuilder;
import com.spring.angular.reddit.service.mail.MailService;
import com.spring.angular.reddit.service.post.PostService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final AuthenticationService authenticationService;
    private final PostService postService;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public CommentServiceImpl(AuthenticationService authenticationService,
                              PostService postService,
                              CommentRepository commentRepository,
                              MailContentBuilder mailContentBuilder,
                              MailService mailService) {
        this.authenticationService = authenticationService;
        this.postService = postService;
        this.commentRepository = commentRepository;
        this.mailContentBuilder = mailContentBuilder;
        this.mailService = mailService;
    }

    @Override
    public void saveComment(Comment comment) throws ServerException {
        Post post = postService.getSinglePost(comment.getPost().getPostId());
        User currentUser = authenticationService.getCurrentUser();

        //bidirectional
        post.getComments().add(comment);
        commentRepository.save(comment);

        String message =
                mailContentBuilder.build(currentUser.getUsername() + " posted a comment on your post.");
        sendCommentNotification(message, post.getUser());
    }

    @Override
    public List<Comment> getAllCommentsForPost(Long postId) throws ServerException {
        Post post = postService.getSinglePost(postId);
        return commentRepository.findByPost(post);
    }

    @Override
    public List<Comment> getAllCommentsForUser(String userName) throws ServerException {
        User user = authenticationService.getUserByUsername(userName);
        return commentRepository.findAllByUser(user);
    }

    private void sendCommentNotification(String message, User user) throws ServerException {
        mailService.sendMail(
                new NotificationEmailResource(user.getUsername() + " Commented on your post", user.getEmail(), message));
    }
}
