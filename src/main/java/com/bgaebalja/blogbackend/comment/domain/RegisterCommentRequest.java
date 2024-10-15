package com.bgaebalja.blogbackend.comment.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterCommentRequest {
    private String email;
    private Long parentId;
    private String content;

    public RegisterCommentRequest(String content){
        this.content = content;
    }
}
