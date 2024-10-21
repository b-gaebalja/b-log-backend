package com.bgaebalja.blogbackend.comment.service;

import com.bgaebalja.blogbackend.comment.domain.Comment;
import com.bgaebalja.blogbackend.comment.domain.RegisterCommentRequest;

import java.util.List;

public interface CommentService {
    List<Comment> getComments(Long postId);
    List<Comment> toComments(Long id);
    Long countComments(Long postId);
    Long createComment(RegisterCommentRequest registerCommentRequest);
    Long modifyComment(RegisterCommentRequest registerCommentRequest, Long commentId);
    Long deleteComment(Long commentId);

}
