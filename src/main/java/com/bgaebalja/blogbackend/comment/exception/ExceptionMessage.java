package com.bgaebalja.blogbackend.comment.exception;

public class ExceptionMessage {
    public static final String COMMENT_NOT_FOUND = "댓글을 찾을 수 없습니다";

    private ExceptionMessage() {
        throw new UnsupportedOperationException();
    }
}
