package com.spring.angular.reddit.controller.post;

import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.Post;
import com.spring.angular.reddit.model.Subreddit;
import com.spring.angular.reddit.model.VoteType;
import com.spring.angular.reddit.resource.PostRequestResource;
import com.spring.angular.reddit.resource.PostResponseResource;
import com.spring.angular.reddit.service.comment.CommentService;
import com.spring.angular.reddit.service.vote.VoteService;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component
public class PostConverter {
    private final CommentService commentService;
    private final VoteService voteService;

    public PostConverter(CommentService commentService, VoteService voteService) {
        this.commentService = commentService;
        this.voteService = voteService;
    }

    public Post toPostEntity(PostRequestResource postRequestResource) {
        Post post = new Post();
        post.setPostName(postRequestResource.getPostName());
        post.setUrl(postRequestResource.getUrl());
        post.setDescription(postRequestResource.getDescription());

        Subreddit subreddit = new Subreddit();
        subreddit.setName(postRequestResource.getSubredditName());
        post.setSubreddit(subreddit);

        return post;
    }

    public PostResponseResource toResource(Post post, int commentCount, VoteType voteType) {
        return PostResponseResource.builder()
                .identifier(post.getIdentifier())
                .postName(post.getPostName())
                .url(post.getUrl())
                .description(post.getDescription())
                .userName(post.getUser().getUsername())
                .subredditName(post.getSubreddit().getName())
                .commentCount(commentCount)
                .createdDate(post.getCreatedDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .upVote(VoteType.UPVOTE.equals(voteType))
                .downVote(VoteType.DOWNVOTE.equals(voteType))
                .voteCount(post.getVoteCount())
                .build();
    }

    public List<PostResponseResource> toResourceList(final List<Post> posts) throws ServerException {
        List<PostResponseResource> postResponseResourceList = new ArrayList<>();
        if (!posts.isEmpty()) {
            for (Post post : posts) {
                int commentCount = commentService.getAllCommentsForPost(post.getIdentifier()).size();
                VoteType voteType = voteService.getVoteType(post);

                PostResponseResource postResponseResource = toResource(post, commentCount, voteType);
                postResponseResourceList.add(postResponseResource);
            }
        }
        return postResponseResourceList;
    }

}
