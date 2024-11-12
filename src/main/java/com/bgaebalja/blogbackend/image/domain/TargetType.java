package com.bgaebalja.blogbackend.image.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TargetType {
    USER("회원"),
    POST("게시글"),
    COMMENT("댓글"),
    ALARM("알림");

    private final String description;
}
