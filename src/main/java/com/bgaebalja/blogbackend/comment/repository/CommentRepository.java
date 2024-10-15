package com.bgaebalja.blogbackend.comment.repository;

import com.bgaebalja.blogbackend.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllCommentsByPostId(Long postId);
    Long countCommentByPostId(Long postId);
}
