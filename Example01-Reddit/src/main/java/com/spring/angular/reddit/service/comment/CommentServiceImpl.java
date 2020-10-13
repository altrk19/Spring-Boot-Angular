package com.spring.angular.reddit.service.comment;


import com.spring.angular.reddit.constants.CommonConstants;
import com.spring.angular.reddit.constants.RequestErrorTypes;
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
import com.spring.angular.reddit.util.KeyGenerationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
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
    @Transactional
    public Comment saveComment(Comment comment) throws ServerException {
        comment.setIdentifier(KeyGenerationUtil.generateUniqueIdentifier());

        //bidirectional
        Post post = postService.getSinglePost(comment.getPost().getIdentifier());
        comment.setPost(post);
        post.getComments().add(comment);

        User user = authenticationService.getCurrentUser();
        user.getComments().add(comment);
        comment.setUser(user);

        commentRepository.save(comment);
        log.debug("comment added successfully with user : {}", user.getUsername());

        String message =
                mailContentBuilder.build(user.getUsername() + " posted a comment on your post.");
        sendCommentNotification(message, post.getUser());

        return comment;
    }

    @Override
    public List<Comment> getAllCommentsForPost(String postIdentifier) throws ServerException {
        Post post = postService.getSinglePost(String.valueOf(postIdentifier));
        return commentRepository.findByPost(post);
    }

    @Override
    public List<Comment> getAllCommentsForUser(String userName) throws ServerException {
        User user = authenticationService.getUserByUsername(userName);
        return commentRepository.findAllByUserId(user.getId());
    }

    @Override
    public Comment getSingleComment(String identifier) throws ServerException {
        return commentRepository.findByIdentifier(identifier).orElseThrow(() -> new ServerException(RequestErrorTypes.UNKNOWN_RESOURCE,
                new String[]{CommonConstants.COMMENT, String.valueOf(String.valueOf(identifier))}, HttpStatus.NOT_FOUND));
    }

    @Override
    public void deleteSingleComment(String identifier) throws ServerException {
        Comment comment = getSingleComment(identifier);
        commentRepository.deleteById(comment.getId());
    }

    private void sendCommentNotification(String message, User user) throws ServerException {
        mailService.sendMail(
                new NotificationEmailResource(user.getUsername() + " Commented on your post", user.getEmail(),
                        message));
    }
}
