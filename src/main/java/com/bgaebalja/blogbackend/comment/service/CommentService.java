package com.bgaebalja.blogbackend.comment.service;

import com.bgaebalja.blogbackend.comment.domain.Comment;
import com.bgaebalja.blogbackend.comment.domain.RegisterCommentRequest;

import java.util.List;

public interface CommentService {
    List<Comment> getComments(Long postId);
    Long countComments(Long postId);
    Long createComment(RegisterCommentRequest registerCommentRequest, Long postId);
    Long modifyComment(RegisterCommentRequest registerCommentRequest, Long commentId);


}
