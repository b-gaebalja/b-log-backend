package com.bgaebalja.blogbackend.comment.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {
    private Long id;
    private String userName;
    private Long postId;
    private String content;
    private Long parentId;
    private LocalDateTime createdAt;

    public CommentResponse() {}
    @Builder
    public CommentResponse(Long id, String userName,Long postId, String content, Long parentId, LocalDateTime createdAt) {
        this.id = id;
        this.userName = userName;
        this.postId = postId;
        this.content = content;
        this.parentId = parentId;
        this.createdAt = createdAt;
    }

}
