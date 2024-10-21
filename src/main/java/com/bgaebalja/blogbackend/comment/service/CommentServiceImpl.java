package com.bgaebalja.blogbackend.comment.service;

import com.bgaebalja.blogbackend.comment.domain.Comment;
import com.bgaebalja.blogbackend.comment.domain.RegisterCommentRequest;
import com.bgaebalja.blogbackend.comment.exception.CommentNoValueException;
import com.bgaebalja.blogbackend.comment.repository.CommentRepository;
import com.bgaebalja.blogbackend.notification.domain.NotificationEventRequest;
import com.bgaebalja.blogbackend.post.domain.Post;
import com.bgaebalja.blogbackend.post.repository.PostRepository;
import com.bgaebalja.blogbackend.user.domain.Users;
import com.bgaebalja.blogbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bgaebalja.blogbackend.comment.exception.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;


    @Override
    @Transactional()
    public List<Comment> getComments(Long postId) {
        return commentRepository.findAllCommentsByPostId(postId);
    }

    @Override
    public List<Comment> toComments(Long id) {
        return commentRepository.findAllById(id);
    }

    @Override
    public Long countComments(Long postId) {
        return commentRepository.countCommentByPostId(postId);
    }

    @Override
    @Transactional
    public Long createComment(RegisterCommentRequest registerCommentRequest) {
        Users user = userRepository.findUsersByEmail(registerCommentRequest.getEmail());

        Post post = postRepository.findById(registerCommentRequest.getPostId()).get();

        Long parentId = registerCommentRequest.getParentId();

        Comment parent = parentId != null
                ? commentRepository.findById(parentId).get()
                : null;

        Comment comment = commentRepository.save(Comment.from(registerCommentRequest, user, post, parent));

        eventPublisher.publishEvent(new NotificationEventRequest(comment.getId()));

        return comment.getId();
    }

    @Transactional()
    @Override
    public Long modifyComment(RegisterCommentRequest registerCommentRequest, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNoValueException(COMMENT_NOT_FOUND));
        comment.modifyContent(registerCommentRequest);

        commentRepository.save(comment);

        return comment.getId();
    }

    public Long deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNoValueException(COMMENT_NOT_FOUND));
        Long postId = comment.getPost().getId();
        comment.deleteComment();

        commentRepository.save(comment);

        return postId;
    }
}
