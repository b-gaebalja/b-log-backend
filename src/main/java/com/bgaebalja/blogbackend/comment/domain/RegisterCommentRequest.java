package com.bgaebalja.blogbackend.comment.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterCommentRequest {
    private String email;
    private Long postId;
    private Long parentId;
    private String content;

    public RegisterCommentRequest(String email, Long postId, Long parentId, String content) {
        this.email = email;
        this.postId = postId;
        this.parentId = parentId;
        this.content = content;
    }

    public RegisterCommentRequest(String content){
        this.content = content;
    }
}
